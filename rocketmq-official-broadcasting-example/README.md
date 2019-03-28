# RocketMQ Official Broadcasting Example #
广播消息示例

What is broadcasting
啥是广播

Broadcasting is sending a message to all subscribers of a topic. If you want all subscribers receive messages about a topic, broadcasting is a good choice.
广播向主题的所有订阅者发送消息。如果您希望所有订阅者都收到有关主题的消息，可以使用广播的方式；

- Producer example
```java
@Component
public class SendMessage implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {

        // 实例化生产者，并设置组名
        DefaultMQProducer producer = DefaultMQProducerSingleton.newInstance();
        producer.start();

        for (int i = 0; i < 15; i++){
            Message msg = new Message("TopicTest",
                    "TagA",
                    "OrderID188",
                    "Hello world".getBytes(RemotingHelper.DEFAULT_CHARSET));
            SendResult sendResult = producer.send(msg);
            System.out.printf("%s%n", "sendResult :" + sendResult);
        }
        producer.shutdown();
    }

}

```

- Consumer example
```java
@Component
public class ConsumerStartRunner implements CommandLineRunner {

    private static final Log logger = LogFactory.getLog(ConsumerStartRunner.class);

    @Override
    public void run(String... args) throws Exception {
        DefaultMQPushConsumer consumer = DefaultMQPushConsumerSingleton.newInstance();

        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        //set to broadcast mode
        consumer.setMessageModel(MessageModel.BROADCASTING);

        consumer.subscribe("TopicTest", "TagA || TagC || TagD");

        consumer.registerMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                            ConsumeConcurrentlyContext context) {
                System.out.printf(Thread.currentThread().getName() + " Receive New Messages: " + msgs + "%n");
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        consumer.start();
        System.out.printf("Broadcast Consumer Started.%n");
    }
}
```