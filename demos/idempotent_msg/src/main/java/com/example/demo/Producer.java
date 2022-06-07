package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;

public class Producer {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws MQClientException, UnsupportedEncodingException, MQBrokerException, RemotingException, InterruptedException, JsonProcessingException {

        DefaultMQProducer producer = new DefaultMQProducer("idempotent_msg");
        producer.setNamesrvAddr("localhost:9876");
        producer.start();

        OrderDTO orderDTO = new OrderDTO("1", "xxx");
        Message msg = new Message("idempotent_msg", "TagA", objectMapper.writeValueAsBytes(orderDTO));
        msg.setKeys("123456"); // 设置幂等id

        SendResult sendResult;
        // send 两次
        sendResult = producer.send(msg);
        System.out.println(sendResult);
        sendResult = producer.send(msg);
        System.out.println(sendResult);

        producer.shutdown();
    }
}
