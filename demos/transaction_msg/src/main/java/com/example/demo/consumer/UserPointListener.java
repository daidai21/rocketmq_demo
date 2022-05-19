package com.example.demo.consumer;

import com.example.demo.BizConst;
import com.example.demo.dto.OrderDTO;
import com.example.demo.services.UserPointService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class UserPointListener {

    @Resource
    private UserPointService userPointService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() throws MQClientException {
        // 实例化消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(BizConst.consumerUserPointGroup);
        // 设置NameServer的地址
        consumer.setNamesrvAddr("localhost:9876");
        // 订阅Topics
        consumer.subscribe(BizConst.topic, "*");

        // 注意消费模型
        consumer.setMessageModel(MessageModel.CLUSTERING);

        // 从头开始消费
        // consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        // 注册消息监听者
        consumer.registerMessageListener(new MessageListenerOrderly() {

            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> messages, ConsumeOrderlyContext consumeOrderlyContext) {

                for (MessageExt message : messages) {
                    try {
                        OrderDTO orderDTO = objectMapper.readValue(message.getBody(), OrderDTO.class);
                        log.info(orderDTO.toString());
                        userPointService.addPointByUser(orderDTO.getUserId());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });
        // 启动消费者
        consumer.start();
    }
}
