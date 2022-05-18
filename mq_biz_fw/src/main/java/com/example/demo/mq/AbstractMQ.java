package com.example.demo.mq;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;


@Getter
@ToString(callSuper = true)
public abstract class AbstractMQ {
    @NonNull
    protected String topic;
}
