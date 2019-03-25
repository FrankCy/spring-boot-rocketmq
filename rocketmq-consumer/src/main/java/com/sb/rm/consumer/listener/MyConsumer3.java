package com.sb.rm.consumer.listener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @description：
 * @author: Yang.Chang
 * @project: spring-boot-rocketmq
 * @package: com.sb.rm.consumer.listener、
 * @email: cy880708@163.com
 * @date: 2019/3/25 下午7:30
 * @mofified By:
 */
@Service
//@RocketMQMessageListener(nameServer = "127.0.0.1:9877", instanceName = "tradeCluster", topic = "test-topic-3", consumerGroup = "my-consumer_test-topic-3")
public class MyConsumer3  {
//        implements RocketMQListener<String> {

    private static final Log logger = LogFactory.getLog(MyConsumer3.class);

//    @Override
//    public void onMessage(String message) {
//        logger.info("received message: " + message);
//    }
}
