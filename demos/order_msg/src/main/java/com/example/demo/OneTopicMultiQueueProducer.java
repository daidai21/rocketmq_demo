package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * 发1k条msgId不断增大的顺序消息
 */
public class OneTopicMultiQueueProducer {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws MQClientException, MQBrokerException, RemotingException, InterruptedException, JsonProcessingException {
        // 消息顺序的id
        long msgId = 0;

        // 实例化一个生产者来产生延时消息
        DefaultMQProducer producer = new DefaultMQProducer("one_topic_multi_queue");
        // 设置NameServer的地址
        producer.setNamesrvAddr("localhost:9876");
        // 启动生产者
        producer.start();

        for (int i = 0; i < 1000; i++) {
            MessageDTO messageDTO = new MessageDTO(msgId);
            msgId++;
            System.out.println("msgId: " + msgId);
            Message message = new Message("one_topic_multi_queue", objectMapper.writeValueAsBytes(messageDTO));
            // 发送消息
            producer.send(message);
        }
        // 关闭生产者
        producer.shutdown();
    }

}
