package com.example.demo.producer;

import com.example.demo.BizConst;
import com.example.demo.dto.OrderDTO;
import com.example.demo.services.ItemService;
import com.example.demo.services.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class OrderTransactionProducer {

    @Resource
    private ItemService itemService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final TransactionMQProducer producer = new TransactionMQProducer(BizConst.producerGroup);

    @PostConstruct
    public void init() throws MQClientException {
        producer.setExecutorService(new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2000), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("client-transaction-msg-check-thread");
                return thread;
            }
        }));
        producer.setTransactionListener(transactionListener);
        producer.setNamesrvAddr("localhost:9876");
        producer.start();
    }

    private TransactionListener transactionListener = new TransactionListener() {

        private AtomicInteger transactionIndex = new AtomicInteger(0);

        @Resource
        private TransactionService transactionService;

        /**
         * itemId：偶数订单会失败，奇数订单会成功
         * @param msg
         * @param arg
         * @return
         */
        @Override
        public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
            LocalTransactionState state = LocalTransactionState.UNKNOW;

            try {
                OrderDTO orderDTO = objectMapper.readValue(msg.getBody(), OrderDTO.class);
                if (itemService.decStockByItemId(orderDTO.getItemId())) {
                    state = LocalTransactionState.COMMIT_MESSAGE;
                } else {
                    state = LocalTransactionState.ROLLBACK_MESSAGE;
                }
                log.info("order orderDTO={} status={}", orderDTO, state);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return state;
        }

        @Override
        public LocalTransactionState checkLocalTransaction(MessageExt msg) {
            LocalTransactionState state = LocalTransactionState.UNKNOW;

            try {
                Boolean isCommitted = transactionService.getTransactionStatusById(msg.getTransactionId());
                if (isCommitted) {
                    state = LocalTransactionState.COMMIT_MESSAGE;
                } else {
                    state = LocalTransactionState.ROLLBACK_MESSAGE;
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            log.info("order status={}", state);
            return state;
        }
    };

    public void sendMsg(OrderDTO orderDTO) throws JsonProcessingException, MQBrokerException, RemotingException, InterruptedException, MQClientException {
        log.info("orderDTO={}", orderDTO);
        Message message = new Message(BizConst.topic, objectMapper.writeValueAsBytes(orderDTO));
        producer.sendMessageInTransaction(message, null);
    }
}
