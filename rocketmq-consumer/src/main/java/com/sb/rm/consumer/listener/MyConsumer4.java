package com.sb.rm.consumer.listener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @description：
 * @author: Yang.Chang
 * @project: spring-boot-rocketmq
 * @package: com.sb.rm.consumer.listener、
 * @email: cy880708@163.com
 * @date: 2019/3/25 下午7:32
 * @mofified By:
 */
@Service
@RocketMQMessageListener(topic = "test-topic-4", consumerGroup = "my-consumer_test-topic-4", consumeMode = ConsumeMode.ORDERLY)
public class MyConsumer4 implements RocketMQListener<String> {

    private static final Log logger = LogFactory.getLog(MyConsumer4.class);

    @Override
    public void onMessage(String message) {
        logger.info("received message: " + message);
        int a = 1 / 0;
        System.out.println("4 a : " + a);
    }
}