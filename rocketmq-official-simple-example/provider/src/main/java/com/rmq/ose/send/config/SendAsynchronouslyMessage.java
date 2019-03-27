package com.rmq.ose.send.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @version 1.0
 * @description：发送异步消息
 * @author: Yang.Chang
 * @project: spring-boot-rocketmq
 * @package: com.rmq.ose.send.config、
 * @email: cy880708@163.com
 * @date: 2019/3/26 下午7:18
 * @mofified By:
 */
@Component
public class SendAsynchronouslyMessage implements CommandLineRunner {

    private static final Log logger = LogFactory.getLog(SendAsynchronouslyMessage.class);

    @Override
    public void run(String... args) throws Exception {
        logger.info("SendAsynchronouslyMessage");
    }

}
