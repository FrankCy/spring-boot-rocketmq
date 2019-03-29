package com.rmq.ote.provider.impl;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @version 1.0
 * @description：
 * @author: Yang.Chang
 * @project: spring-boot-rocketmq
 * @package: com.rmq.ote.provider.impl、
 * @email: cy880708@163.com
 * @date: 2019/3/29 上午11:14
 * @mofified By:
 */
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
                    // 准备
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
