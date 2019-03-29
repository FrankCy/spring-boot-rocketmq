package com.rmq.ote.provider.config;

import com.rmq.ote.provider.impl.TransactionListenerImpl;
import com.sb.rm.common.util.DefaultTransactionMQProducerSingleton;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.*;

/**
 * @version 1.0
 * @description：
 * @author: Yang.Chang
 * @project: spring-boot-rocketmq
 * @package: com.rmq.ote.provider.config、
 * @email: cy880708@163.com
 * @date: 2019/3/29 上午10:06
 * @mofified By:
 */
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
