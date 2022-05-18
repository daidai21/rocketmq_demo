package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.io.IOException;
import java.util.List;

public class SingleQueueOrderConsumer {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // 上一次消费的msgId
    private static long lastMsgId = -1;

    public static void main(String[] args) throws MQClientException {

        // 实例化消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("single_queue_order");
        // 设置NameServer的地址
        consumer.setNamesrvAddr("localhost:9876");
        // 订阅Topics
        consumer.subscribe("single_queue_order", "*");

        // 从头开始消费
        // consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        // 注册消息监听者
        consumer.registerMessageListener(new MessageListenerOrderly() { // 注意类选择： MessageListenerOrderly，而不是 MessageListenerConcurrently

            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> messages, ConsumeOrderlyContext consumeOrderlyContext) {

                for (MessageExt message : messages) {
                    long curMsgId;
                    MessageDTO messageDTO = new MessageDTO();
                    try {
                        messageDTO = objectMapper.readValue(message.getBody(), MessageDTO.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    curMsgId = messageDTO.getId();
                    if (lastMsgId == -1) {
                        System.out.println("cur consumer msgId: " + curMsgId);
                    } else if (curMsgId < lastMsgId) {
                        System.out.println("msg disorder curMsgId: " + curMsgId + "; lastMsgId: " + lastMsgId);
                    } else {
                        System.out.println("cur consumer msgId: " + curMsgId);
                    }
                    lastMsgId = curMsgId;
                }
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });
        // 启动消费者
        consumer.start();
    }
}
