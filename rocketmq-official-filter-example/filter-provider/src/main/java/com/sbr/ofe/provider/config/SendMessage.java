package com.sbr.ofe.provider.config;

import com.sb.rm.common.util.DefaultMQProducerSingleton;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @version 1.0
 * @description：
 * @author: Yang.Chang
 * @project: spring-boot-rocketmq
 * @package: com.sbr.ofe.provider.config、
 * @email: cy880708@163.com
 * @date: 2019/3/28 下午6:18
 * @mofified By:
 */
@Component
public class SendMessage implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {

        // 实例化生产者，并设置组名
        DefaultMQProducer producer = DefaultMQProducerSingleton.newInstance();
        producer.start();

        int totalMessagesToSend = 20;

        for (int i = 0; i < totalMessagesToSend; i++) {
            Message message = new Message("TestTopic", ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            // 设置过滤条件，每条消息都有[a,i]，消费时判断a的区间
            message.putUserProperty("a", String.valueOf(i));
            // 发送消息
            SendResult sendResult = producer.send(message);
            System.out.println("sendResult : " + sendResult);
        }

        producer.shutdown();
    }
}
