package com.example.demo;


import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

public class Producer {
    public static void main(String[] args) throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        // 实例化一个生产者来产生延时消息
        DefaultMQProducer producer = new DefaultMQProducer("delay_msg");
        // 设置NameServer的地址
        producer.setNamesrvAddr("localhost:9876");
        // 启动生产者
        producer.start();
        int totalMessagesToSend = 100;
        for (int i = 0; i < totalMessagesToSend; i++) {
            Message message = new Message("delay_msg", ("Hello scheduled message " + i).getBytes());
            // 设置延时等级3,这个消息将在5s之后发送(现在只支持固定的几个时间,详看delayTimeLevel)
            message.setDelayTimeLevel(2);
            // 发送消息
            producer.send(message);
        }
        // 关闭生产者
        producer.shutdown();

    }
}
