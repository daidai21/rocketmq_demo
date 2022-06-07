package com.example.demo;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;

public class OneProducerMultiTopic {
    public static void main(String[] args) throws MQBrokerException, RemotingException, InterruptedException, MQClientException, UnsupportedEncodingException {

        DefaultMQProducer producer = new DefaultMQProducer("one_group_multi_topic");
        producer.setNamesrvAddr("localhost:9876");
        producer.start();
        Message msg = new Message("one_group_multi_topic_1", "TagA", ("Hello RocketMQ one_group_multi_topic_1").getBytes(RemotingHelper.DEFAULT_CHARSET));
        SendResult sendResult = producer.send(msg);
        System.out.printf("%s%n", sendResult);

        Message msg1 = new Message("one_group_multi_topic_2", "TagA", ("Hello RocketMQ one_group_multi_topic_2").getBytes(RemotingHelper.DEFAULT_CHARSET));
        SendResult sendResult1 = producer.send(msg1);
        System.out.printf("%s%n", sendResult1);

        producer.shutdown();

    }
}
