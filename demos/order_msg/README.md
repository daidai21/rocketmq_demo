# order msg

### multi queue order

by sharding key. default is 4 read/write queue of 1 topic.

run result log: [link](./multi_queue_order_consumer.log)

### single queue order

1. http://localhost:8180/#/topic
2. create topic `single_queue_order`
```text
集群名： DefaultCluster
BROKER_NAME: broker-a
主题名： single_queue_order
写队列数量： 1
读队列数量： 1
perm： 6  # 支持同时读写
```
3. run `SingleQueueOrderProducer.java`
4. run `SingleQueueOrderConsumer.java`

### 1 topic n queue

1. run `OneTopicMultiQueueProducer.java`
2. run `OneTopicMultiQueueConsumer.java`

run result like:
```shell
cur consumer msgId: 15
cur consumer msgId: 13
cur consumer msgId: 5
cur consumer msgId: 8
cur consumer msgId: 6
cur consumer msgId: 7
cur consumer msgId: 9
cur consumer msgId: 10
cur consumer msgId: 3
cur consumer msgId: 12
cur consumer msgId: 11
msg disorder curMsgId: 4; lastMsgId: 5
cur consumer msgId: 16
cur consumer msgId: 14
msg disorder curMsgId: 1; lastMsgId: 13
msg disorder curMsgId: 0; lastMsgId: 13
cur consumer msgId: 17
msg disorder curMsgId: 2; lastMsgId: 15
cur consumer msgId: 18
cur consumer msgId: 19
```

### other

* rocketmq的queue 就是 kafka的partition
* replica是副本数，和queue/partition不是一个概念

### ref

* [Kafka基本结构和关键指标](https://shibinfei.github.io/2018/05/19/Kafka%20-%20Overview/)
