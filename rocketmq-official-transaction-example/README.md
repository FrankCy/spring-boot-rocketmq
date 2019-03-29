# RocketMQ Official Transaction Example #
事务队列（用于控制事务操作）
**```两阶段提交消息实现，确保分布式系统的最终一致性（是最终一致性），事务性消息可以以原子性方式执行本地事务的发送消息和消费消息。```**
## 规约 ##
- 不支持定时消息，不支持批处理消息
- 单条消息检查限制15次，可通过修改transactionCheckMax来更改检查次数最大值，如果检查次数超过最大检查次数，会放弃此消息并打印错误日志（可通过覆盖"AbstractTransactionCheckListener"类进行修改）
- broker（经纪人）中通过参数"transactionTimeout"配置多久之后开始检查消息，也可以在发送消息时，通过设置用户属性"CHECK_IMMUNITY_TIME_IN_SECONDS"来更改限制（此参数优先级在"transactionMsgTimeout"参数之上）
- 可多次检查或者消费消息
- 单点情况下会有失败情况发生，RocketMQ高可用性机制确保了高可用性，如果想取保事务性消息不会丢失并保证事务完整性，可采用同步双写机制
- 事务消息生产者ID不允许与其它类型消息生产者ID相同。与其它类型消息生产者不同，事务性消息允许执行后查询情况，可通过MQ Server按生产者ID查找

## 三种事务消息状态 ##
- 提交事务
TransactionStatus.CommitTransaction（通过检查，允许消费者使用此消息）
- 回滚事务
TransactionStatus.RollbackTransaction（未通过检查，该消息会被删除，并不允许使用）
- 中间状态
TransactionStatus.Unknown（该消息正在检查中，等待检查结果后执行上述两个状态）

## 示例 ##
- 创建生产者
通过TransactionMQProducer创建客户端，producerGroup不能与其它类型消息生产者相同，并设置自定义线程池来处理检查请求。执行本地事务之后，需要根据结果执行事务性MQ（根据三种状态回复请求方）
```java
@Component
public class SendMessage implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {

        TransactionListener transactionListener = new TransactionListenerImpl();

        TransactionMQProducer producer = DefaultTransactionMQProducerSingleton.newInstance();

        /**
         *
         * * * * * * * —— ThreadPoolExecutor —— * * * * * * * *
         *
         * 创建线程池消费者
         *
         * @param corePoolSize 消费最小线程数
         * @param maximumPoolSize 消费最大线程数
         * @param keepAliveTime 线程活跃时间
         * @param unit keepAliveTime时间单位
         * @param workQueue 任务之前保存任务的队列容量
         * @throws IllegalArgumentException if one of the following holds:<br>
         *         {@code corePoolSize < 0}<br>
         *         {@code keepAliveTime < 0}<br>
         *         {@code maximumPoolSize <= 0}<br>
         *         {@code maximumPoolSize < corePoolSize}
         * @throws NullPointerException if {@code workQueue} is null
         */
        ExecutorService executorService = new ThreadPoolExecutor(
                2,
                5,
                100,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(2000),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName("client-transaction-msg-check-thread");
                        return thread;
                    }
                 }
         );

        producer.setExecutorService(executorService);
        producer.setTransactionListener(transactionListener);
        producer.start();

        String[] tags = new String[] {"TagA", "TagB", "TagC", "TagD", "TagE"};

        for (int i = 0; i < 10; i++) {
            try {
                Message msg = new Message(
                        "TopicTest",
                        tags[i % tags.length],
                        "KEY" + i,
                        ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET)
                );
                SendResult sendResult = producer.sendMessageInTransaction(msg, null);
                System.out.printf("%s%n", sendResult);

                Thread.sleep(10);
            } catch (MQClientException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < 100000; i++) {
            Thread.sleep(1000);
        }

        producer.shutdown();
    }

}

```
- 自定义线程池检查请求
```java
public class TransactionListenerImpl implements TransactionListener {

    private AtomicInteger transactionIndex = new AtomicInteger(0);

    private ConcurrentHashMap<String, Integer> localTrans = new ConcurrentHashMap<>();

    /**
     * @description：
     * 当send transactional prepare（half）消息成功时，将调用此方法执行本地事务。
     * @version 1.0
     * @author: Yang.Chang
     * @email: cy880708@163.com
     * @date: 2019/3/29 上午11:20
     * @mofified By:
     * @param msg（准备）消息
     * @param arg 自定义业务参数
     */
    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        int value = transactionIndex.getAndIncrement();
        int status = value % 3;
        localTrans.put(msg.getTransactionId(), status);
        return LocalTransactionState.UNKNOW;
    }

    /**
     * @description：
     * 当本地消息没有响应时，经纪人将发送请求检查消息，检查本地事务执行状态，从而获取本地事务状态。
     * @version 1.0
     * @author: Yang.Chang
     * @email: cy880708@163.com
     * @date: 2019/3/29 上午11:21
     * @mofified By:
     * @param msg 检查消息
     * @return  交易状态
     */
    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        Integer status = localTrans.get(msg.getTransactionId());
        if (null != status) {
            switch (status) {
                case 0:
                    // 检查中
                    return LocalTransactionState.UNKNOW;
                case 1:
                    // 提交消息
                    return LocalTransactionState.COMMIT_MESSAGE;
                case 2:
                    // 回滚消息
                    return LocalTransactionState.ROLLBACK_MESSAGE;
            }
        }
        return LocalTransactionState.COMMIT_MESSAGE;
    }
}

```
- 创建消费者（普通消费即可，事务性消息控制在提交时）
```java
@Component
public class ConsumerRunner implements CommandLineRunner {

    private static final Log logger = LogFactory.getLog(ConsumerRunner.class);

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

