package com.nac.abc.controller;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

//定时器
@Component
public class TimerController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    //    每两天凌晨0点除法
    @Scheduled(cron= "0 0 0 */2 * ? *")
    public void demo(){

    }

}
