package com.rmq.ose.send.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @version 1.0
 * @description：
 * @author: Yang.Chang
 * @project: spring-boot-rocketmq
 * @package: com.rmq.ose.send.config、
 * @email: cy880708@163.com
 * @date: 2019/3/27 下午5:01
 * @mofified By:
 */
@Component
public class SendSynchronouslyMessage implements CommandLineRunner {

    private static final Log logger = LogFactory.getLog(SendSynchronouslyMessage.class);

    @Override
    public void run(String... args) throws Exception {
        logger.info("SendSynchronouslyMessage");
    }

}
