package com.example.demo;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class BatchSendSplitMsgListProducer {

    /**
     * 业务调用
     *
     * @param args
     * @throws MQClientException
     */
    public static void main(String[] args) throws MQClientException, UnsupportedEncodingException {
        String topic = "batch_send_msg_split";
        List<Message> msgs = new ArrayList<>();
        for (int i = 0; i < 10000; ++i) {
            msgs.add(new Message(topic, "*", ("msg context id" + i).getBytes(RemotingHelper.DEFAULT_CHARSET)));
        }
        BatchSendSplitMsgListProducer producer = new BatchSendSplitMsgListProducer();
        producer.send(msgs);
    }

    private final DefaultMQProducer producer;

    public BatchSendSplitMsgListProducer() throws MQClientException {
        // 实例化一个生产者来产生延时消息
        producer = new DefaultMQProducer("batch_msg");
        // 设置NameServer的地址
        producer.setNamesrvAddr("localhost:9876");
        // 启动生产者
        producer.start();
    }

    /**
     * 抽出来的业务接口
     *
     * @param msgs 这里的msgs业务代码调用的时候就不需要关注msgs的过长的问题
     */
    public void send(List<Message> msgs) {
        //把大的消息分裂成若干个小的消息
        MsgListSplitter splitter = new MsgListSplitter(msgs);
        while (splitter.hasNext()) {
            try {
                List<Message> listItem = splitter.next();
                producer.send(listItem);
                System.out.println("split slice send " + listItem.size() + "...");
            } catch (Exception e) {
                e.printStackTrace();
                //处理error
            }
        }
    }
}

