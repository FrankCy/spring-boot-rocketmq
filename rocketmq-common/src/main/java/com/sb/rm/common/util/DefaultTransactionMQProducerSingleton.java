package com.sb.rm.common.util;

import org.apache.rocketmq.client.producer.TransactionMQProducer;

/**
 * @version 1.0
 * @description：
 * @author: Yang.Chang
 * @project: spring-boot-rocketmq
 * @package: com.sb.rm.common.util、
 * @email: cy880708@163.com
 * @date: 2019/3/29 上午11:24
 * @mofified By:
 */
public class DefaultTransactionMQProducerSingleton {
    private static class SingletonHolder {
        private static TransactionMQProducer defaultTransactionMQProducer = new TransactionMQProducer("FRANK_GROUP");
    }

    private DefaultTransactionMQProducerSingleton() {
    }

    public static TransactionMQProducer newInstance() {
        DefaultTransactionMQProducerSingleton.SingletonHolder.defaultTransactionMQProducer.setNamesrvAddr("localhost:9876");
        return DefaultTransactionMQProducerSingleton.SingletonHolder.defaultTransactionMQProducer;
    }
}
