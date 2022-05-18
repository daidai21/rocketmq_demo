# lose msg

### producer

**sync send**

1. retry send msg
2. print err log
3. monitor alarm
4. save to DB (then debug re-send msg later)
TODO： send to defer_queue? rocketmq support?

**async send**

1. callback retry send msg
2. callback print err log
3. callback monitor alarm
4. callback save to DB (then debug re-send msg later)

### consumer

1. create topic `lose_msg`
```text
集群名： DefaultCluster  # Default
BROKER_NAME: broker-a  # Default
主题名： lose_msg
写队列数量： 4  # Default
读队列数量： 4  # Default
perm： 6  # 支持同时读写   # Default
```
2. console send one msg
```text
标签：
值：
消息主体: retry_msg_ctx
```
3. run `RetryConsumer.java`
4. console find retry_queue
   * 死信队列的topic是 `%DLQ%lose_msg`
   * 重试队列的topic是 `%RETRY%lose_msg`
   * 默认队列的topic是 `lose_msg`
