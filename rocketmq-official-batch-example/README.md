# RocketMQ Official Batch Example #
发送批量消息

Why batch? <br/>
啥是群发？
批量发送有效提高了单条多次消息发送的性能，应用在批处理情况；

Usage constraints <br/>
```使用限制``` <br/>
Messages of the same batch should have: same topic, same waitStoreMsgOK and no schedule support. <br/>

**```同一批次消息应该具有：相同主题、相同的waitStoreMsgOK和没有计划支持。```**
Besides, the total size of the messages in one batch should be no more than 1MiB.<br/>
**```同一批次消息大小不能超过1M```**

## 批量发送方式一 ##
How to use batch？ <br/>
如何使用批次？ <br/>
If you just send messages of no more than 1MiB at a time, it is easy to use batch:<br/>
**```只要不超过1M即可```**

## 批量发送方式二 ##
Split into lists<br/>
拆分列表
The complexity only grow when you send large batch and you may not sure if it exceeds the size limit (1MiB). <br/>
在发送时不确定它的大小是不是超过1M。
At this time, you’d better split the lists: <br/>
最好拆分发送。

- Provider
```java
@Component
public class SendMessage implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        // Instantiate a producer to send scheduled messages
        DefaultMQProducer producer = DefaultMQProducerSingleton.newInstance();

        // Launch producer
        producer.start();

        String topic = "TestTopic";
        List<Message> messages = new ArrayList<>();
        messages.add(new Message(topic, "TagA", "OrderID001", "Hello world 0".getBytes()));
        messages.add(new Message(topic, "TagA", "OrderID002", "Hello world 1".getBytes()));
        messages.add(new Message(topic, "TagA", "OrderID003", "Hello world 2".getBytes()));


        /**
         * 方式一 ：定义List，发送批量消息
         */
        try {
            producer.send(messages);
        } catch (Exception e) {
            e.printStackTrace();
            //handle the error
        }

        /**
         * 方式二 ： 直接封装List，一条一条发（在不清楚批次是否超过1M时，要进行拆分发送）
         */
        //then you could split the large list into small ones:
        ListSplitter splitter = new ListSplitter(messages);
        while (splitter.hasNext()) {
            try {
                List<Message>  listItem = splitter.next();
                producer.send(listItem);
            } catch (Exception e) {
                e.printStackTrace();
                //handle the error
            }
        }

        // 发送之后停止发送
        producer.shutdown();
    }
}
```

- Consumer（批量消费）
```java
@Component
public class ConsumerRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        DefaultMQPushConsumer consumer = DefaultMQPushConsumerSingleton.newInstance();
        consumer.setConsumeMessageBatchMaxSize(10);
        /**
         * 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费<br>
         * 如果非第一次启动，那么按照上次消费的位置继续消费
         */
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        consumer.subscribe("TestTopic", "*");

        consumer.registerMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {

                try {
                    System.out.println("msgs的长度" + msgs.size());
                    System.out.println(Thread.currentThread().getName() + " Receive New Messages: " + msgs);
                } catch (Exception e) {
                    e.printStackTrace();
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }

                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        consumer.start();

        System.out.println("Consumer Started.");
    }
}
```