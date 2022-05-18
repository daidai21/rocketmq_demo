package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.List;

/**
 * 订单号是自增的 0 ~ 999
 */
public class MultiQueueOrderProducer {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws MQClientException, MQBrokerException, RemotingException, InterruptedException, JsonProcessingException {
        // 消息顺序的id
        long msgId = 0;

        // 实例化一个生产者来产生消息
        DefaultMQProducer producer = new DefaultMQProducer("multi_queue_order");
        // 设置NameServer的地址
        producer.setNamesrvAddr("localhost:9876");
        // 启动生产者
        producer.start();

        for (int i = 0; i < 1000; i++) {
            MessageDTO messageDTO = new MessageDTO(msgId);
            msgId++;
            // System.out.println("msgId: " + msgId);
            Message message = new Message("multi_queue_order", objectMapper.writeValueAsBytes(messageDTO));
            // 发送消息
            SendResult sendResult = producer.send(message, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message message, Object arg) {
                    Long id = (Long) arg; // 获取订单id
                    long idx = id % mqs.size(); // 计算要发送的queueId
                    return mqs.get((int) idx);
                }
            }, messageDTO.getId()); // 订单id
            System.out.println(String.format("SendResult status:%s, queueId:%d, body:%s",
                    sendResult.getSendStatus(), sendResult.getMessageQueue().getQueueId(), messageDTO));
        }
        // 关闭生产者
        producer.shutdown();
    }

}
