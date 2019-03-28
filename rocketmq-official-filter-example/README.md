# RocketMQ Official Filter Example #
RocketMQ 官方示例 —— 过滤

Filter Example <br/>
In most cases, tag is a simple and useful design to select message you want. For example: <br/>

过滤示例 <br/>
通常，Tag已经满足我们的需求，用于选择所需消息，例如：
```java
// 选择TOPIC下 "TAGA"、"TAGB"、"TAGC"
DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("CID_EXAMPLE");
consumer.subscribe("TOPIC", "TAGA || TAGB || TAGC");
```

The consumer will recieve messages that contains TAGA or TAGB or TAGC. But the limitation is that one message only can have one tag, and this may not work for sophisticated scenarios. In this case, you can use SQL expression to filter out messages. <br/>
这样消费者将包含在TAGA、TAGB、TAGC中的消息都获取消费，不过这只能满足一种情况，就是全部要消费，在实际工作中，我们可以使用"SQL表达式"来过滤消息，达到特定业务处理。

Principle <br/>
原理：<br/>
SQL feature could do some calculation through the properties you put in when sending messages. Under the grammars defined by RocketMQ, you can implement some interesting logic. Here is an example: <br/>
使用SQL，可以在发送消息时指定一些条件规则，然后在消费消息时对规则进行判断，指定消费哪些信息。

```java
------------
| message  |
|----------|  a > 5 AND b = 'abc'
| a = 10   |  --------------------> Gotten
| b = 'abc'|
| c = true |
------------
------------
| message  |
|----------|   a > 5 AND b = 'abc'
| a = 1    |  --------------------> Missed
| b = 'abc'|
| c = true |
------------
```

- 规范： <br/>
RocketMQ only defines some basic grammars to support this feature. You could also extend it easily. <br/>
RocketMQ仅支持一些简单的SQL基本语法。

**支持的一些语法**

||说明|
|:--|:--|
|数字比较|">"、">="、"<"、"<="、"BETWEEN"、"="|
|字符比较|=、<>、IN|
|判空|IS NULL 、 IS NOT NULL|
|逻辑判断|AND、OR、NOT|

**常用类型**

||举例|
|:--|:--|
|数字|如123、3.1415926|
|字符|如'abc'，必须是单引号|
|特殊常量|NULL|
|布尔|TRUE、FALSE|

- 使用限制 <br/>
**```只有消费者可以使用SQL92规范进行过滤。```**
```java
public void subscribe(final String topic, final MessageSelector messageSelector)
```

## 例子 ##
- 发送消息<br/>
发送消息时，通过putUserProperty设置条件
```java
@Component
public class SendMessage implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {

        // 实例化生产者，并设置组名
        DefaultMQProducer producer = DefaultMQProducerSingleton.newInstance();
        producer.start();

        int totalMessagesToSend = 20;

        for (int i = 0; i < totalMessagesToSend; i++) {
            Message message = new Message("TestTopic", ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            // 设置过滤条件，每条消息都有[a,i]，消费时判断a的区间
            message.putUserProperty("a", String.valueOf(i));
            // 发送消息
            SendResult sendResult = producer.send(message);
            System.out.println("sendResult : " + sendResult);
        }

        producer.shutdown();
    }
}
```

- 消费消息<br/>
消费消息石，通过MessageSelector.bySql判断条件，规范是SQL92
```java
@Component
public class ConsumerRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        DefaultMQPushConsumer consumer = DefaultMQPushConsumerSingleton.newInstance();
        // 根据userProperty，判断只获取 a>=0 并且 a<=3 的消息
        consumer.subscribe("TestTopic", MessageSelector.bySql("a between 0 and 3"));
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                System.out.println(Thread.currentThread().getName() + " Receive New Messages: " + msgs);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
    }
}
```