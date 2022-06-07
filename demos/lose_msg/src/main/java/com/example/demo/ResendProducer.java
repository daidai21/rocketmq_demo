package com.example.demo;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.nio.charset.StandardCharsets;

/**
 * re-send msg
 * <p>
 * FIXME: not support exec, only demo
 */
public class ResendProducer {

    public static void main(String[] args) throws MQClientException, MQBrokerException, RemotingException, InterruptedException {

        // 实例化一个生产者来产生消息
        DefaultMQProducer producer = new DefaultMQProducer("lose_msg");
        // 设置NameServer的地址
        producer.setNamesrvAddr("localhost:9876");
        // 启动生产者
        producer.start();

        // 消息内容
        String msg = "msg ...";

        Message message = new Message("multi_queue_order", msg.getBytes(StandardCharsets.UTF_8));
        // 发送消息
        SendResult sendResult = producer.send(message);

        // 检查消息发送结果
        if (sendResult.getSendStatus() != SendStatus.SEND_OK) {
            // 打error日志
            System.out.println(String.format("SendResult status:%s, queueId:%d, body:%s",
                    sendResult.getSendStatus(), sendResult.getMessageQueue().getQueueId(), msg));

            // 重试发送...

            // 监控告警...

            // 保存到DB...

        }

        // 关闭生产者
        producer.shutdown();
    }
}
