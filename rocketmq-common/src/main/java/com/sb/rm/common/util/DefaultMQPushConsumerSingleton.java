package com.sb.rm.common.util;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;

/**
 * @version 1.0
 * @description：
 * @author: Yang.Chang
 * @project: spring-boot-rocketmq
 * @package: com.sb.rm.common.util、
 * @email: cy880708@163.com
 * @date: 2019/3/27 下午6:28
 * @mofified By:
 */
public class DefaultMQPushConsumerSingleton {
    private static class SingletonHolder {
        private static DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer("FRANK_GROUP");
    }

    private DefaultMQPushConsumerSingleton() {
    }

    public static DefaultMQPushConsumer newInstance() {
        DefaultMQPushConsumerSingleton.SingletonHolder.defaultMQPushConsumer.setNamesrvAddr("localhost:9876");
        return DefaultMQPushConsumerSingleton.SingletonHolder.defaultMQPushConsumer;
    }
}
