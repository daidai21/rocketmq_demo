# idempotent msg 

* run `Producer.java`
```shell
SendResult [sendStatus=SEND_OK, msgId=7F0000013E0618B4AAC223CD21300000, offsetMsgId=C0A80B6A00002A9F0000000000A95D02, messageQueue=MessageQueue [topic=idempotent_msg, brokerName=broker-a, queueId=3], queueOffset=3]
SendResult [sendStatus=SEND_OK, msgId=7F0000013E0618B4AAC223CD21300000, offsetMsgId=C0A80B6A00002A9F0000000000A95DD8, messageQueue=MessageQueue [topic=idempotent_msg, brokerName=broker-a, queueId=0], queueOffset=1]
```
* run `Consumer.java`
```shell
==========
consumer message: MessageExt [brokerName=broker-a, queueId=3, storeSize=214, queueOffset=4, sysFlag=0, bornTimestamp=1654613670151, bornHost=/172.29.0.1:61626, storeTimestamp=1654613670176, storeHost=/192.168.11.106:10911, msgId=C0A80B6A00002A9F0000000000A9605A, commitLogOffset=11100250, bodyCRC=1062890479, reconsumeTimes=0, preparedTransactionOffset=0, toString()=Message{topic='idempotent_msg', flag=0, properties={MIN_OFFSET=0, MAX_OFFSET=6, KEYS=123456, UNIQ_KEY=7F0000013E3718B4AAC223D08D070000, CLUSTER=DefaultCluster, TAGS=TagA}, body=[123, 34, 105, 100, 34, 58, 34, 49, 34, 44, 34, 105, 110, 102, 111, 34, 58, 34, 120, 120, 120, 34, 125], transactionId='null'}]
consumer orderDTO: OrderDTO(id=1, info=xxx)
saveResult: true


==========
consumer message: MessageExt [brokerName=broker-a, queueId=3, storeSize=214, queueOffset=5, sysFlag=0, bornTimestamp=1654613703445, bornHost=/172.29.0.1:61642, storeTimestamp=1654613703453, storeHost=/192.168.11.106:10911, msgId=C0A80B6A00002A9F0000000000A963B2, commitLogOffset=11101106, bodyCRC=1062890479, reconsumeTimes=0, preparedTransactionOffset=0, toString()=Message{topic='idempotent_msg', flag=0, properties={MIN_OFFSET=0, MAX_OFFSET=6, KEYS=123456, UNIQ_KEY=7F0000013E5318B4AAC223D10F140000, CLUSTER=DefaultCluster, TAGS=TagA}, body=[123, 34, 105, 100, 34, 58, 34, 49, 34, 44, 34, 105, 110, 102, 111, 34, 58, 34, 120, 120, 120, 34, 125], transactionId='null'}]
consumer orderDTO: OrderDTO(id=1, info=xxx)
saveResult: false



```
