package com.sbr.ofe.consumer.config;

import com.sb.rm.common.util.DefaultMQPushConsumerSingleton;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @version 1.0
 * @description：
 * @author: Yang.Chang
 * @project: spring-boot-rocketmq
 * @package: com.sbr.ofe.consumer.config、
 * @email: cy880708@163.com
 * @date: 2019/3/28 下午6:19
 * @mofified By:
 */
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
