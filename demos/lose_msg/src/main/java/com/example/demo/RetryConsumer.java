package com.example.demo;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * retry consumer msg
 */
public class RetryConsumer {

    private static int consumerFailedTimes = 0;

    public static void main(String[] args) throws MQClientException {
        // 实例化消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("lose_msg");
        // 设置NameServer的地址
        consumer.setNamesrvAddr("localhost:9876");
        // 订阅Topics
        consumer.subscribe("lose_msg", "*");

        // 设置消费失败5次进入死信队列
        consumer.setMaxReconsumeTimes(3);

        // 注册消息监听者
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messages, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                for (MessageExt message : messages) {
                    System.out.println("consumer failed msg: " + message);
                }
                consumerFailedTimes++;
                if (consumerFailedTimes > 10) { // 消费10次失败就退出了
                    System.out.println("consumer failed then exit.");
                    consumer.shutdown();
                }
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        });
        // 启动消费者
        consumer.start();
    }
}
