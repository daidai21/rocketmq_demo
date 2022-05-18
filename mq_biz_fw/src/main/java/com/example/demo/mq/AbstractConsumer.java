package com.example.demo.mq;

import com.example.demo.exceptions.MQInitConfigException;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.io.Serializable;
import java.util.List;

@Getter
@ToString(callSuper = true)
public abstract class AbstractConsumer extends AbstractMQ {

    @NonNull
    private final String consumerGroup;

    @NonNull
    private final String expression;

    /**
     * 是并行消费还是单线程消费
     * 默认是并行消费
     */
    @NonNull
    private final Boolean isOrder;

    private DefaultMQPushConsumer consumer;

    public abstract <T extends Serializable> void receiver(T msgObj);

    private void init() {
        try {
            consumer = new DefaultMQPushConsumer(this.consumerGroup);
            consumer.subscribe(this.topic, this.expression);
            if (this.isOrder) {
                consumer.registerMessageListener(new MessageListenerOrderly() {

                    @Override
                    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext) {
                        for (MessageExt messageExt : list) {
                            receiver(messageExt);
                        }
                        return ConsumeOrderlyStatus.SUCCESS;
                    }
                });
            } else {
                consumer.registerMessageListener(new MessageListenerConcurrently() {
                    @Override
                    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                        for (MessageExt messageExt : list) {
                            receiver(messageExt);
                        }
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }
                });
            }
            consumer.start();

        } catch (MQClientException e) {
            throw new MQInitConfigException();
        }
    }

    public AbstractConsumer(String topic, String consumerGroup) {
        this.topic = topic;
        this.consumerGroup = consumerGroup;
        this.expression = "*";
        this.isOrder = false;

        init();
    }

    public AbstractConsumer(String topic, String consumerGroup, String expression) {
        this.topic = topic;
        this.consumerGroup = consumerGroup;
        this.expression = expression;
        this.isOrder = false;

        init();
    }

    public AbstractConsumer(String topic, String consumerGroup, String expression, Boolean isOrder) {
        this.topic = topic;
        this.consumerGroup = consumerGroup;
        this.expression = expression;
        this.isOrder = isOrder;

        init();
    }
}
