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

public class SendMsgByPropertyProducer {
    public static void main(String[] args) throws MQClientException, UnsupportedEncodingException, MQBrokerException, RemotingException, InterruptedException {
        // 实例化消息生产者Producer
        DefaultMQProducer producer = new DefaultMQProducer("filter_msg_by_property");
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
        String topic = "filter_msg_by_property";
        List<Message> messages = new ArrayList<>();

        // 出口 低价 订单
        Message message1 = new Message(topic, "Hello world 0 export".getBytes(RemotingHelper.DEFAULT_CHARSET));
        message1.putUserProperty("type", "export");
        message1.putUserProperty("level", "low");
        messages.add(message1);

        // 进口 中价 订单
        Message message2 = new Message(topic, "Hello world 1 export".getBytes(RemotingHelper.DEFAULT_CHARSET));
        message2.putUserProperty("type", "import");
        message2.putUserProperty("level", "mid");
        messages.add(message2);

        // 进口 高价 订单
        Message message3 = new Message(topic, "Hello world 0 export".getBytes(RemotingHelper.DEFAULT_CHARSET));
        message3.putUserProperty("type", "import");
        message3.putUserProperty("level", "high");
        messages.add(message3);

        return messages;
    }
}
