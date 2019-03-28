package com.sbr.obe.provider.config;

import com.sb.rm.common.util.DefaultMQProducerSingleton;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0
 * @description：
 * @author: Yang.Chang
 * @project: spring-boot-rocketmq
 * @package: com.sbr.obe.provider.config、
 * @email: cy880708@163.com
 * @date: 2019/3/28 下午4:46
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

        String topic = "TestTopic";
        List<Message> messages = new ArrayList<>();
        messages.add(new Message(topic, "TagA", "OrderID001", "Hello world 0".getBytes()));
        messages.add(new Message(topic, "TagA", "OrderID002", "Hello world 1".getBytes()));
        messages.add(new Message(topic, "TagA", "OrderID003", "Hello world 2".getBytes()));


        /**
         * 方式一 ：定义List，发送批量消息
         */
        try {
            SendResult sendResult = producer.send(messages);
            System.out.printf("%s%n", "一次性发送批量信息： " + sendResult);
        } catch (Exception e) {
            e.printStackTrace();
            //handle the error
        }

        /**
         * 方式二 ： 直接封装List，一条一条发（在不清楚批次是否超过1M时，要进行拆分发送）
         */
        //then you could split the large list into small ones:
        ListSplitter splitter = new ListSplitter(messages);
        while (splitter.hasNext()) {
            try {
                List<Message>  listItem = splitter.next();
                SendResult sendResultfor = producer.send(listItem);
                System.out.printf("%s%n", "分割多次发送批量信息[1M]： " + sendResultfor);
            } catch (Exception e) {
                e.printStackTrace();
                //handle the error
            }
        }

        // 发送之后停止发送
        producer.shutdown();
    }
}
