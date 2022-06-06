
先启动 ReceiverMsgByFilterTagConsumer 消费， 然后再启动 SendMsgByTagProducer

交易订单分业务、分营销类型、分正逆向、分地域、分物流商等打属性，然后可以sql过滤订阅。

一条消息只支持一个tag，但是一条消息可以支持多个property。
