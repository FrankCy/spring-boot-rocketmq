package com.rmq.ose.send.config;

import com.sb.rm.common.util.DefaultMQProducerSingleton;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @version 1.0
 * @description：同步消息
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
        DefaultMQProducer producer = DefaultMQProducerSingleton.newInstance();
        producer.setVipChannelEnabled(false);
        // 启动实例
        producer.start();
        for (int i = 0; i < 10; i++) {
            // 创建消息，并指定主题、标记、消息内容
            Message msg = new Message(
                    /*主题*/
                    "TopicTest",
                    /*所属标记*/
                    "TagA",
                    /*发送的消息*/
                    ("单例模式创建生产者 " + i).getBytes(RemotingHelper.DEFAULT_CHARSET)
            );
            //发送消息(调用生产者配置的经纪人（broker）进行发送)
            SendResult sendResult = producer.send(msg);
            logger.info("SendSynchronouslyMessage sendResult " + sendResult);
        }
        //生产者使用后必须关闭
        producer.shutdown();
        logger.info("SendSynchronouslyMessage");
    }

}
