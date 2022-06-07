package com.example.demo;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.PullCallback;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


public class Consumer {
    public static void main(String[] args) throws RemotingException, InterruptedException, MQClientException {
        DefaultMQPullConsumer defaultMQPullConsumer = new DefaultMQPullConsumer();
        defaultMQPullConsumer.setNamesrvAddr("localhost:9876");
        defaultMQPullConsumer.setVipChannelEnabled(false);
        defaultMQPullConsumer.setConsumerGroup("CID_RMQ_SYS_LMQ_TEST");
        defaultMQPullConsumer.setInstanceName("CID_RMQ_SYS_LMQ_TEST");
        defaultMQPullConsumer.setRegisterTopics(new HashSet<>(Arrays.asList("lmq_msg")));
        defaultMQPullConsumer.setBrokerSuspendMaxTimeMillis(2000);
        defaultMQPullConsumer.setConsumerTimeoutMillisWhenSuspend(3000);
        defaultMQPullConsumer.start();

        String brokerName = "broker-a";
        MessageQueue mq = new MessageQueue("%LMQ%123", brokerName, 0);
        defaultMQPullConsumer.getDefaultMQPullConsumerImpl().getRebalanceImpl().getmQClientFactory().updateTopicRouteInfoFromNameServer("lmq_msg");

        Thread.sleep(30000);
        Long offset = defaultMQPullConsumer.maxOffset(mq);

        defaultMQPullConsumer.pullBlockIfNotFound(mq, "*", offset, 32, new PullCallback() {
            @Override
            public void onSuccess(PullResult pullResult) {
                List<MessageExt> list = pullResult.getMsgFoundList();
                if (list == null || list.isEmpty()) {
                    return;
                }
                for (MessageExt messageExt : list) {
                    System.out.println(messageExt);
                }
            }

            @Override
            public void onException(Throwable e) {

            }
        });
    }
}
