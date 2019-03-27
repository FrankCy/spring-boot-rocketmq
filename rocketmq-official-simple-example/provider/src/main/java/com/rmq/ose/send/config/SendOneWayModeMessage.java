package com.rmq.ose.send.config;

import com.sb.rm.common.util.DefaultMQProducerSingleton;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @version 1.0
 * @description：One-way transmission is used for cases requiring moderate reliability, such as log collection.
 * 【用于单向消息发送，在日志收集或者无需响应的情况下使用】
 * @author: Yang.Chang
 * @project: spring-boot-rocketmq
 * @package: com.rmq.ose.send.config、
 * @email: cy880708@163.com
 * @date: 2019/3/26 下午7:20
 * @mofified By:
 */
@Component
public class SendOneWayModeMessage implements CommandLineRunner {

    private static final Log logger = LogFactory.getLog(SendOneWayModeMessage.class);

    @Override
    public void run(String... args) throws Exception {
        //Instantiate with a producer group name.
        DefaultMQProducer producer = DefaultMQProducerSingleton.newInstance();
        //Launch the instance.
        producer.start();
        for (int i = 0; i < 10; i++) {
            //Create a message instance, specifying topic, tag and message body.
            Message msg = new Message(
                    /* Topic */
                    "TopicTest" ,
                    /* Tag */
                    "TagA" ,
                    /* Message body */
                    ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET)
            );
            //Call send message to deliver message to one of brokers.
            producer.sendOneway(msg);
        }
        //Shut down once the producer instance is not longer in use.
        producer.shutdown();

        logger.info("SendOneWayModeMessage");
    }
}
