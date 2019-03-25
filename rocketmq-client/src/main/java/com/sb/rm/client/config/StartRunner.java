package com.sb.rm.client.config;

import com.sb.rm.common.po.OrderPaidEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @version 1.0
 * @description：
 * @author: Yang.Chang
 * @project: spring-boot-rocketmq
 * @package: com.sb.rm.config、
 * @email: cy880708@163.com
 * @date: 2019/3/25 下午6:04
 * @mofified By:
 */
@Component
public class StartRunner implements CommandLineRunner {

    private static final Log logger = LogFactory.getLog(StartRunner.class);

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Override
    public void run(String... args) throws Exception {

        rocketMQTemplate.convertAndSend("test-topic-1", "Hello, World!");

        rocketMQTemplate.send("test-topic-1", MessageBuilder.withPayload("Hello, World! I'm from spring message").build());

        rocketMQTemplate.convertAndSend("test-topic-2", new OrderPaidEvent("T_001", new BigDecimal("88.00")));
//
////        rocketMQTemplate.destroy(); // notes:  once rocketMQTemplate be destroyed, you can not send any message again with this rocketMQTemplate
//
//        // 如下两种方式等价
//        rocketMQTemplate.convertAndSend("test-topic-1", "Hello, World!");
//
//        rocketMQTemplate.send("test-topic-1", MessageBuilder.withPayload("Hello, World! I'm from spring message").build());
//
//        // 第三个参数为key
//        rocketMQTemplate.syncSend("test-topic-1", "Hello, World! I'm from simple message", 10086L);
//
//        // topic: ORDER，tag: paid, cacel
//        rocketMQTemplate.convertAndSend("ORDER:paid", "Hello, World!");
//        rocketMQTemplate.convertAndSend("ORDER:cancel", "Hello, World!");
//
//        // 消息体为自定义对象
//        rocketMQTemplate.convertAndSend("test-topic-2", new OrderPaidEvent("T_001", new BigDecimal("88.00")));
//
//        // 发送即发即失消息（不关心发送结果）
//        rocketMQTemplate.sendOneWay("test-topic-1", MessageBuilder.withPayload("I'm one way message").build());
//
//
//        // 发送顺序消息
//        rocketMQTemplate.syncSendOrderly("test-topic-4", "I'm order message", "1234");
//
//        // 发送异步消息
//        rocketMQTemplate.asyncSend("test-topic-1", MessageBuilder.withPayload("I'm one way message").build(), new SendCallback() {
//            @Override
//            public void onSuccess(SendResult sendResult) {
//
//            }
//
//            @Override
//            public void onException(Throwable e) {
//
//            }
//        });

        System.out.println("send finished!");

    }
}
