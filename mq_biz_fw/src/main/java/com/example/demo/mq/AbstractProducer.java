package com.example.demo.mq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
@ToString(callSuper = true)
public abstract class AbstractProducer extends AbstractMQ {

    @NonNull
    private String producerGroup;

    private DefaultMQProducer producer;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private void init() {
        this.producer = new DefaultMQProducer(this.producerGroup);
    }

    public AbstractProducer(String topic, String producerGroup) {
        this.topic = topic;
        this.producerGroup = producerGroup;
    }

    public <T extends Serializable> void send(T msgObj) {
        send(msgObj, null);
    }

    public <T extends Serializable> void send(T msgObj, String tags) {
        send(msgObj, tags, new HashMap<>());
    }

    public <T extends Serializable, P extends Serializable> void send(T msgObj, String tags, Map<String, P> properties) {
        try {
            Message message = new Message(this.topic, tags, objectMapper.writeValueAsBytes(msgObj));
            for (Map.Entry<String, P> entry : properties.entrySet()) {
                message.putUserProperty(entry.getKey(), entry.getValue().toString());
            }
            this.producer.send(message);

        } catch (JsonProcessingException | MQBrokerException | RemotingException | InterruptedException | MQClientException e) {
            e.printStackTrace();
        }
    }

}
