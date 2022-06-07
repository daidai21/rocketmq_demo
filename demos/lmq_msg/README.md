# LMQ

存储一条消息可以被多个队列消费。

### exec

* run `Producer.java`
* run `Consumer.java`


```shell
MessageExt [brokerName=broker-a, queueId=0, storeSize=259, queueOffset=20, sysFlag=0, bornTimestamp=1654610892685, bornHost=/172.29.0.1:61518, storeTimestamp=1654610892695, storeHost=/192.168.11.106:10911, msgId=C0A80B6A00002A9F0000000000A8069C, commitLogOffset=11011740, bodyCRC=869529788, reconsumeTimes=0, preparedTransactionOffset=0, toString()=Message{topic='lmq_msg', flag=0, properties={MIN_OFFSET=0, MAX_OFFSET=82, INNER_MULTI_DISPATCH=%LMQ%123,%LMQ%456, UNIQ_KEY=7F000001391518B4AAC223A62B8C0048, CLUSTER=DefaultCluster, TAGS=TagA, INNER_MULTI_QUEUE_OFFSET=81,81}, body=[72, 101, 108, 108, 111, 32, 82, 111, 99, 107, 101, 116, 77, 81, 32, 55, 50], transactionId='null'}]
```

### ref

* https://github.com/apache/rocketmq/blob/master/docs/cn/Example_LMQ.md
* https://github.com/apache/rocketmq/pull/3694
