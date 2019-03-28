package com.sbr.obe.consumer.config;

import com.sb.rm.common.util.DefaultMQPushConsumerSingleton;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @version 1.0
 * @description：
 * @author: Yang.Chang
 * @project: spring-boot-rocketmq
 * @package: com.sbr.obe.consumer.config、
 * @email: cy880708@163.com
 * @date: 2019/3/28 下午4:23
 * @mofified By:
 */
@Component
public class ConsumerRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        // Instantiate message consumer
        DefaultMQPushConsumer consumer = DefaultMQPushConsumerSingleton.newInstance();

        // Subscribe topics
        consumer.subscribe("TestTopic", "*");

        // Register message listener
//        consumer.registerMessageListener(new MessageListenerConcurrently() {
//            @Override
//            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messages, ConsumeConcurrentlyContext context) {
//                for (MessageExt message : messages) {
//                    // Print approximate delay time period
//                    System.out.println("Receive message[msgId=" + message.getMsgId() + "] "
//                            + (System.currentTimeMillis() - message.getStoreTimestamp()) + "ms later");
//                }
//                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
//            }
//        });

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
