package com.sb.rm.common.util;

import org.apache.rocketmq.client.producer.DefaultMQProducer;

/**
 * @version 1.0
 * @description：
 * @author: Yang.Chang
 * @project: spring-boot-rocketmq
 * @package: com.sb.rm.common.util、
 * @email: cy880708@163.com
 * @date: 2019/3/27 下午5:56
 * @mofified By:
 */
public class DefaultMQProducerSingleton {
    private static class SingletonHolder {
        private static DefaultMQProducer defaultMQProducer = new DefaultMQProducer();
    }

    private DefaultMQProducerSingleton() {
    }

    public static DefaultMQProducer newInstance() {
//        SingletonHolder.defaultMQProducer.set
        return SingletonHolder.defaultMQProducer;
    }

}
