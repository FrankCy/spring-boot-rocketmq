package com.rmq.ote.consumer.config;

import com.sb.rm.common.util.DefaultMQPushConsumerSingleton;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @version 1.0
 * @description：
 * @author: Yang.Chang
 * @project: spring-boot-rocketmq
 * @package: com.rmq.ote.consumer.config、
 * @email: cy880708@163.com
 * @date: 2019/3/29 上午10:07
 * @mofified By:
 */
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
