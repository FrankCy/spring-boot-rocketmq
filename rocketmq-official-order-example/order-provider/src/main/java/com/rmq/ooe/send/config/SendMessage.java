package com.rmq.ooe.send.config;

import com.sb.rm.common.util.DefaultMQProducerSingleton;
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
 * RocketMQ使用FIFO（First in，First out 先进先出）顺序提供有序消息
 * 示例演示了发送/接收全局和分区排序的消息
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
        MQProducer producer = DefaultMQProducerSingleton.newInstance();

        // 启动实例
        producer.start();
        String[] tags = new String[] {"TagA", "TagB", "TagC", "TagD", "TagE"};
        for (int i = 0; i < 10; i++) {
            int orderId = i % 10;
            // 创建消息，设置Topic，内容
            Message msg = new Message("TopicTest", tags[i % tags.length], "KEY" + i,
                    ("Hello RocketMQ " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    Integer id = (Integer) arg;
                    int index = id % mqs.size();
                    return mqs.get(index);
                }
            }, orderId);

            System.out.printf("%s%n", sendResult);
        }
        // 发送后关闭生产者
        producer.shutdown();
    }

}
