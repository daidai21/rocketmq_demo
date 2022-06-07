# multi topic msg


* run `OneProducerMultiTopic.java`
```shell
SendResult [sendStatus=SEND_OK, msgId=7F0000014B2818B4AAC2245ADEC00000, offsetMsgId=C0A80B6A00002A9F0000000000A9655E, messageQueue=MessageQueue [topic=one_group_multi_topic_1, brokerName=broker-a, queueId=2], queueOffset=0]
SendResult [sendStatus=SEND_OK, msgId=7F0000014B2818B4AAC2245ADF020001, offsetMsgId=C0A80B6A00002A9F0000000000A96640, messageQueue=MessageQueue [topic=one_group_multi_topic_2, brokerName=broker-a, queueId=2], queueOffset=0]
```

* run `OneConsumerMultiTopic.java`
```shell
Receive message[msgId=7F0000014B2818B4AAC2245ADF020001] TagA Hello RocketMQ one_group_multi_topic_2
Receive message[msgId=7F0000014B2818B4AAC2245ADEC00000] TagA Hello RocketMQ one_group_multi_topic_1
```

### ref

* https://help.aliyun.com/document_detail/43523.html
