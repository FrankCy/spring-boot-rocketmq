# RocketMQ Official Schedule Example #
预定消息

What is scheduled message?
啥是预定消息

Scheduled messages differ from normal messages in that they won’t be delivered until a provided time later. <br/>
预定消息与普通消息区别在于，在发送消息之前定义消费者

- Start consumer to wait for incoming subscribed messages<br/>
启动预定消费者，等待接收消息
```java
@Component
public class ConsumerRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        // Instantiate message consumer
        DefaultMQPushConsumer consumer = DefaultMQPushConsumerSingleton.newInstance();

        // Subscribe topics
        consumer.subscribe("TestTopic", "*");

        // Lambda表达式，与上面一个意思
        consumer.registerMessageListener((MessageListenerConcurrently) (messages, context) -> {
            for (MessageExt message : messages) {
                // Print approximate delay time period
                System.out.println("Receive message[msgId=" + message.getMsgId() + "] "
                        + (System.currentTimeMillis() - message.getStoreTimestamp()) + "ms later");
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });

        // Launch consumer
        consumer.start();
    }

}
```

- Send scheduled messages<br/>
发送预定消息
```java
@Component
public class SendMessage implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        // Instantiate a producer to send scheduled messages
        DefaultMQProducer producer = DefaultMQProducerSingleton.newInstance();

        // Launch producer
        producer.start();
        int totalMessagesToSend = 20;
        for (int i = 0; i < totalMessagesToSend; i++) {
            Message message = new Message("TestTopic", ("Hello scheduled message " + i).getBytes());
            // 该消息10秒后发送
            message.setDelayTimeLevel(3);
            // 发送消息
            producer.send(message);
        }

        // 发送之后停止发送
        producer.shutdown();
    }
}
```