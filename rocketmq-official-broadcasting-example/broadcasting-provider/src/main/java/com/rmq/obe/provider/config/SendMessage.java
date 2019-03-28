package com.rmq.obe.provider.config;

import com.sb.rm.common.util.DefaultMQProducerSingleton;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @version 1.0
 * @description：
 * 广播发送消息
 * @author: Yang.Chang
 * @project: spring-boot-rocketmq
 * @package: com.rmq.ooe.send.config、
 * @email: cy880708@163.com
 * @date: 2019/3/28 下午2:42
 * @mofified By:
 */
@Component
public class SendMessage implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {

        // 实例化生产者，并设置组名
        DefaultMQProducer producer = DefaultMQProducerSingleton.newInstance();
        producer.start();

        for (int i = 0; i < 15; i++){
            Message msg = new Message("TopicTest",
                    "TagA",
                    "OrderID188",
                    "Hello world".getBytes(RemotingHelper.DEFAULT_CHARSET));
            SendResult sendResult = producer.send(msg);
            System.out.printf("%s%n", "sendResult :" + sendResult);
        }
        producer.shutdown();
    }

}
