package com.sb.rm.client.scheduled;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @version 1.0
 * @description：
 * @author: Yang.Chang
 * @project: spring-boot-rocketmq
 * @package: com.sb.rm.client.scheduled、
 * @email: cy880708@163.com
 * @date: 2019/3/25 下午8:20
 * @mofified By:
 */
@Component
public class ScheduledTask {

    private static final Log logger = LogFactory.getLog(ScheduledTask.class);

    @Scheduled(cron = "0/7 * * * *")
    public void scheduled(){
        logger.info("=====>>>>>使用cron  {}" + System.currentTimeMillis());
    }

}
