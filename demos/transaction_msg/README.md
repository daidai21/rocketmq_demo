# transaction msg

### desc

itemId=1有10个库存，itemId=2有0个库存。

事务消息，consumer是不感知的，只有producer是需要改造的（发送半消息 -> commit消息 两步骤）

只关注核心事务是否能成功就行，例如创单只关注 订单状态 + 库存扣减状态，用户积分是非核心事务，失败也所谓，后续回补就行。

### run

1. run MQ platform `cd deploy/local && ./start.sh`
2. run `OnlineSellerApplication.java`
3. chrome open URL

`http://localhost:8080/order/create?itemId=1&userId=123` 事务成功
```shell
2022-05-19 15:05:55.485  INFO 51570 --- [nio-8080-exec-2] c.example.demo.OnlineSellerApplication   : ================================================================================
2022-05-19 15:05:55.485  INFO 51570 --- [nio-8080-exec-2] c.example.demo.OnlineSellerApplication   : start process...
2022-05-19 15:05:55.485  INFO 51570 --- [nio-8080-exec-2] c.example.demo.OnlineSellerApplication   : ================================================================================
2022-05-19 15:05:55.485  INFO 51570 --- [nio-8080-exec-2] c.example.demo.OnlineSellerApplication   : itemId=1, userId=123
2022-05-19 15:05:55.485  INFO 51570 --- [nio-8080-exec-2] com.example.demo.services.OrderService   : 创建订单 itemId=1, userId=123
2022-05-19 15:05:55.485  INFO 51570 --- [nio-8080-exec-2] c.e.d.producer.OrderTransactionProducer  : orderDTO=OrderDTO(super=com.example.demo.dto.OrderDTO@9ab3d, userId=123, orderId=2, itemId=1)
2022-05-19 15:05:55.490  INFO 51570 --- [nio-8080-exec-2] com.example.demo.services.ItemService    : 库存扣减 itemId=1
2022-05-19 15:05:55.490  INFO 51570 --- [nio-8080-exec-2] c.e.d.producer.OrderTransactionProducer  : order orderDTO=OrderDTO(super=com.example.demo.dto.OrderDTO@9ab3d, userId=123, orderId=2, itemId=1) status=COMMIT_MESSAGE
2022-05-19 15:06:21.438  INFO 51570 --- [MessageThread_1] c.e.demo.consumer.UserPointListener      : OrderDTO(super=com.example.demo.dto.OrderDTO@9ab3d, userId=123, orderId=2, itemId=1)
2022-05-19 15:06:21.438  INFO 51570 --- [MessageThread_1] c.e.demo.services.UserPointService       : 给用户增加积分 userId=123
```

`http://localhost:8080/order/create?itemId=2&userId=456` 事务失败

```shell
2022-05-19 15:07:17.912  INFO 51570 --- [nio-8080-exec-5] c.example.demo.OnlineSellerApplication   : ================================================================================
2022-05-19 15:07:17.912  INFO 51570 --- [nio-8080-exec-5] c.example.demo.OnlineSellerApplication   : start process...
2022-05-19 15:07:17.912  INFO 51570 --- [nio-8080-exec-5] c.example.demo.OnlineSellerApplication   : ================================================================================
2022-05-19 15:07:17.912  INFO 51570 --- [nio-8080-exec-5] c.example.demo.OnlineSellerApplication   : itemId=2, userId=456
2022-05-19 15:07:17.912  INFO 51570 --- [nio-8080-exec-5] com.example.demo.services.OrderService   : 创建订单 itemId=2, userId=456
2022-05-19 15:07:17.912  INFO 51570 --- [nio-8080-exec-5] c.e.d.producer.OrderTransactionProducer  : orderDTO=OrderDTO(super=com.example.demo.dto.OrderDTO@1b5b7e, userId=456, orderId=3, itemId=2)
2022-05-19 15:07:17.918  INFO 51570 --- [nio-8080-exec-5] com.example.demo.services.ItemService    : 库存扣减 itemId=2
2022-05-19 15:07:17.918  INFO 51570 --- [nio-8080-exec-5] c.e.d.producer.OrderTransactionProducer  : order orderDTO=OrderDTO(super=com.example.demo.dto.OrderDTO@1b5b7e, userId=456, orderId=3, itemId=2) status=ROLLBACK_MESSAGE
```

### ref

* [基于RocketMQ分布式事务 - 完整示例](https://zhuanlan.zhihu.com/p/115553176)
* [事务消息](https://help.aliyun.com/document_detail/43348.html)
* https://github.com/apache/rocketmq/blob/master/docs/cn/RocketMQ_Example.md#6-%E6%B6%88%E6%81%AF%E4%BA%8B%E5%8A%A1%E6%A0%B7%E4%BE%8B
* https://github.com/hosaos/transaction-message-demo
