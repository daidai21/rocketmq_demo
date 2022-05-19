package com.example.demo.services;

import com.example.demo.dto.OrderDTO;
import com.example.demo.producer.OrderTransactionProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
public class OrderService {

    private static final AtomicLong orderId = new AtomicLong(0);

    @Resource
    private OrderTransactionProducer producer;

    /**
     * 订单创建服务
     *
     * @param itemId
     * @param userId
     * @return orderId
     */
    public Long create(Long itemId, Long userId) throws MQBrokerException, RemotingException, JsonProcessingException, InterruptedException, MQClientException {
        log.info("创建订单 itemId={}, userId={}", itemId, userId);
        producer.sendMsg(new OrderDTO(userId, orderId.incrementAndGet(), itemId));
        // 这里把扣库存写在 OrderTransactionProducer 中了
        return orderId.get();
    }
}
