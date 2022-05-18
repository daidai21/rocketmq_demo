package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.io.IOException;
import java.util.List;

/**
 * 接收消息，因为是无序的消息，所以有可能收到的消息msgId小于上一个消息的msgId
 */
public class OneTopicMultiQueueConsumer {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // 上一次消费的msgId
    private static long lastMsgId = -1;

    public static void main(String[] args) throws MQClientException {

        // 实例化消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("one_topic_multi_queue");
        // 设置NameServer的地址
        consumer.setNamesrvAddr("localhost:9876");
        // 订阅Topics
        consumer.subscribe("one_topic_multi_queue", "*");

        // 注册消息监听者
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messages, ConsumeConcurrentlyContext context) {
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
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        // 启动消费者
        consumer.start();

    }
}
