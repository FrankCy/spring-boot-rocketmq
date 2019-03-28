# RocketMQ Official Simple Example #
官方示例，简单的MQ

DefaultMQProducer【MQ生产者】

## 示例 ##
- 发送同步消息
```java
@Component
public class SendSynchronouslyMessage implements CommandLineRunner {

    private static final Log logger = LogFactory.getLog(SendSynchronouslyMessage.class);

    @Override
    public void run(String... args) throws Exception {
        DefaultMQProducer producer = DefaultMQProducerSingleton.newInstance();
        producer.setVipChannelEnabled(false);
        // 启动实例
        producer.start();
        for (int i = 0; i < 10; i++) {
            // 创建消息，并指定主题、标记、消息内容
            Message msg = new Message(
                    /*主题*/
                    "TopicTest",
                    /*所属标记*/
                    "TagA",
                    /*发送的消息*/
                    ("单例模式创建生产者 " + i).getBytes(RemotingHelper.DEFAULT_CHARSET)
            );
            //发送消息(调用生产者配置的经纪人（broker）进行发送)
            SendResult sendResult = producer.send(msg);
            logger.info("SendSynchronouslyMessage sendResult " + sendResult);
        }
        //生产者使用后必须关闭
        producer.shutdown();
        logger.info("SendSynchronouslyMessage");
    }
}

```
- 发送异步消息
```java
@Component
public class SendAsynchronouslyMessage implements CommandLineRunner {

    private static final Log logger = LogFactory.getLog(SendAsynchronouslyMessage.class);

    @Override
    public void run(String... args) throws Exception {
        //Instantiate with a producer group name.
        DefaultMQProducer producer =  DefaultMQProducerSingleton.newInstance();
        //Launch the instance.
        producer.start();
        producer.setRetryTimesWhenSendAsyncFailed(0);
        for (int i = 0; i < 10; i++) {
            final int index = i;
            //Create a message instance, specifying topic, tag and message body.
            Message msg = new Message(
                    "TopicTest",
                    "TagB",
                    "OrderID188",
                    "Hello world".getBytes(RemotingHelper.DEFAULT_CHARSET));
            producer.send(msg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.printf("%-10d OK %s %n", index,
                            sendResult.getMsgId());
                }
                @Override
                public void onException(Throwable e) {
                    System.out.printf("%-10d Exception %s %n", index, e);
                    e.printStackTrace();
                }
            });
        }
        //Shut down once the producer instance is not longer in use.
        producer.shutdown();
        logger.info("SendAsynchronouslyMessage");
    }

}
```
- 发送单向消息
```java
@Component
public class SendOneWayModeMessage implements CommandLineRunner {

    private static final Log logger = LogFactory.getLog(SendOneWayModeMessage.class);

    @Override
    public void run(String... args) throws Exception {
        //Instantiate with a producer group name.
        DefaultMQProducer producer = DefaultMQProducerSingleton.newInstance();
        //Launch the instance.
        producer.start();
        for (int i = 0; i < 10; i++) {
            //Create a message instance, specifying topic, tag and message body.
            Message msg = new Message(
                    /* Topic */
                    "TopicTest" ,
                    /* Tag */
                    "TagA" ,
                    /* Message body */
                    ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET)
            );
            //Call send message to deliver message to one of brokers.
            producer.sendOneway(msg);
        }
        //Shut down once the producer instance is not longer in use.
        producer.shutdown();

        logger.info("SendOneWayModeMessage");
    }
}

```

- 消费消息
```java
@Component
public class ConsumerStartRunner implements CommandLineRunner {

    private static final Log logger = LogFactory.getLog(ConsumerStartRunner.class);

    @Override
    public void run(String... args) throws Exception {
        DefaultMQPushConsumer consumer = DefaultMQPushConsumerSingleton.newInstance();

        // 配置消费途径
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

        // 配置消费目标
        consumer.subscribe("TopicTest", "*");

        // 消费监听
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                logger.info(Thread.currentThread().getName() + "Receive New Message : " + msgs);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        consumer.start();
    }
}
```