package com.sbr.obe.provider.config;

import com.sb.rm.common.util.DefaultMQProducerSingleton;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @version 1.0
 * @description：
 * @author: Yang.Chang
 * @project: spring-boot-rocketmq
 * @package: com.sbr.obe.provider.config、
 * @email: cy880708@163.com
 * @date: 2019/3/28 下午4:22
 * @mofified By:
 */
@Component
public class SendMessage implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        // Instantiate a producer to send scheduled messages
        DefaultMQProducer producer = DefaultMQProducerSingleton.newInstance();

        // Launch producer
        producer.start();
        int totalMessagesToSend = 20;
        for (int i = 0; i < totalMessagesToSend; i++) {
            Message message = new Message("TestTopic", ("Hello scheduled message " + i).getBytes());
            // 该消息10秒后发送
            message.setDelayTimeLevel(3);
            // 发送消息
            producer.send(message);
        }

        // 发送之后停止发送
        producer.shutdown();
    }
}
