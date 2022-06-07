package com.example.demo;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;

public class Producer {
    public static void main(String[] args) throws MQClientException, MQBrokerException, RemotingException, InterruptedException, UnsupportedEncodingException {
        DefaultMQProducer producer = new DefaultMQProducer("lmq_msg_producer");
        producer.setNamesrvAddr("localhost:9876");
        producer.start();

        for (int i = 0; i < 1000; ++i) {
            // 创建消息
            Message msg = new Message("lmq_msg", "TagA", ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            /*
             * INNER_MULTI_DISPATCH property and PREFIX must start as "%LMQ%",
             * If it is multiple LMQ, need to use “,” split
             */
            // 投递到`123`和`456`队列
            msg.putUserProperty("INNER_MULTI_DISPATCH", "%LMQ%123,%LMQ%456");
            // 发送msg
            SendResult sendResult = producer.send(msg);
            System.out.println(sendResult);

            Thread.sleep(500);
        }
    }
}
