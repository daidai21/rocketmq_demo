# rmq demo

本地docker 启动

### deploy

* [本地调试部署](./deploy/local/README.md)

### demos

* [发送消息](./demos/send_msg/)
    * [同步发送消息](./demos/send_msg/src/main/java/com/example/demo/SyncSendMessageProducer.java)
    * [异步](./demos/send_msg/src/main/java/com/example/demo/ASyncSendMessageProducer.java)
    * [单向](./demos/send_msg/src/main/java/com/example/demo/OneWaySendMessageProducer.java)
* 顺序消息
    * 生产
        * 全局有序
        * 分区顺序
    * 消费
        * 全局有序
        * 分区顺序
* [延迟消息](./demos/delay_msg/src/main/java/com/example/demo/)
* 消息可靠性
    * 发送不丢失： 消息重投
    * 存储不丢失 这里不管
    * 消费不丢失： 消息重试
* 死信队列
* 回溯消费
* 批量消息
    * 发送
    * 消费
* 过滤消息
* 事务消息
* 日志格式

### ref

* https://github.com/apache/rocketmq/blob/master/docs/cn/RocketMQ_Example.md
