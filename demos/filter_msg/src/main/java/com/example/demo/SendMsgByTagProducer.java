package com.example.demo;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 这里使用交易订单样例
 * https://stackoverflow.com/questions/43365394/can-rocketmq-producer-send-message-with-mutiple-tags
 * https://github.com/apache/rocketmq/issues/2454
 */
public class SendMsgByTagProducer {
    public static void main(String[] args) throws MQClientException, UnsupportedEncodingException, MQBrokerException, RemotingException, InterruptedException {
        // 实例化消息生产者Producer
        DefaultMQProducer producer = new DefaultMQProducer("filter_msg");
        // 设置NameServer的地址
        producer.setNamesrvAddr("localhost:9876");
        // 启动Producer实例
        producer.start();

        List<Message> messages = buildOrderMsgs();

        // 发送消息到一个Broker
        SendResult sendResult = producer.send(messages);
        // 通过sendResult返回消息是否成功送达
        System.out.printf("%s%n", sendResult);

        // 如果不再发送消息，关闭Producer实例。
        producer.shutdown();
    }

    private static List<Message> buildOrderMsgs() throws UnsupportedEncodingException {
        // 创建消息，并指定Topic，Tag和消息体
        String topic = "filter_msg";
        List<Message> messages = new ArrayList<>();

        // 进出口订单标tag
        messages.add(new Message(topic, "export", "OrderID001", "Hello world 0 export".getBytes(RemotingHelper.DEFAULT_CHARSET)));
        messages.add(new Message(topic, "import", "OrderID002", "Hello world 1 import".getBytes(RemotingHelper.DEFAULT_CHARSET)));
        messages.add(new Message(topic, "export", "OrderID003", "Hello world 2 export".getBytes(RemotingHelper.DEFAULT_CHARSET)));
        return messages;
    }
}
