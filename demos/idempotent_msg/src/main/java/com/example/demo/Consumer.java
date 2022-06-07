package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.io.IOException;
import java.util.List;


public class Consumer {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) throws MQClientException {

        final OrderRepository orderRepository = new OrderRepository();

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("idempotent_msg");
        consumer.setNamesrvAddr("localhost:9876");
        consumer.subscribe("idempotent_msg", "*");

        // 单线程顺序消费
        consumer.setConsumeThreadMax(1);
        consumer.setConsumeThreadMin(1);

        consumer.registerMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                for (MessageExt message : msgs) {
                    OrderDTO orderDTO = new OrderDTO();
                    try {
                        orderDTO = objectMapper.readValue(message.getBody(), OrderDTO.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    System.out.println("\n\n==========");
                    System.out.println("consumer message: " + message);
                    System.out.println("consumer orderDTO: " + orderDTO);
                    boolean saveResult = orderRepository.saveOrder(orderDTO.getId(), orderDTO.getInfo());
                    System.out.println("saveResult: " + saveResult);
                }
                return ConsumeOrderlyStatus.SUCCESS;

            }
        });
        consumer.start();
    }
}
