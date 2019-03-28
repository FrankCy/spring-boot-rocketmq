package com.rmq.ose.send.config;

import com.sb.rm.common.util.DefaultMQProducerSingleton;
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
        //Instantiate with a producer group name.
        DefaultMQProducer producer =  DefaultMQProducerSingleton.newInstance();
        //Launch the instance.
        producer.start();
        producer.setRetryTimesWhenSendAsyncFailed(0);
        for (int i = 0; i < 10; i++) {
            final int index = i;
            //Create a message instance, specifying topic, tag and message body.
            Message msg = new Message(
                    "TopicTest",
                    "TagB",
                    "OrderID188",
                    "Hello world".getBytes(RemotingHelper.DEFAULT_CHARSET));
            producer.send(msg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.printf("%-10d OK %s %n", index,
                            sendResult.getMsgId());
                }
                @Override
                public void onException(Throwable e) {
                    System.out.printf("%-10d Exception %s %n", index, e);
                    e.printStackTrace();
                }
            });
        }
        //Shut down once the producer instance is not longer in use.
        producer.shutdown();
        logger.info("SendAsynchronouslyMessage");
    }

}
