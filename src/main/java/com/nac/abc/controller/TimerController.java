package com.nac.abc.controller;

import com.nac.abc.entity.User;
import com.nac.abc.service.impl.CommodityServiceImpl;
import com.nac.abc.service.impl.GoodsServiceImpl;
import com.nac.abc.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

//定时器
@Component
public class TimerController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private CommodityServiceImpl commodityService;

    @Autowired
    private GoodsServiceImpl goodsService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    //每天凌晨0点分别将当天可售出商品数量和可添加失物招领数量置3
    @Scheduled(cron = "0 0 0 * * ?")
    public void setDailyLimits(){
        List<User> userList = userService.selectAllUserExceptAdmin();
        userList.forEach(user -> {
            stringRedisTemplate.opsForHash().put("user.getId()","commodity","3");
            stringRedisTemplate.opsForHash().put("user.getId()","goods","3");
            stringRedisTemplate.expire("user.getId()", 24, TimeUnit.HOURS);
        });
    }

    //每天凌晨零时零分将商品日期减一
    @Scheduled(cron= "0 0 0 * * ?")
    public void minusCommodityOutdated(){
        commodityService.minusCommodityOutdated();
    }

    //每天凌晨零时五分将属性为已售状态的商品逻辑删除
    @Scheduled(cron = "0 5 0 * * ?")
    private void deleteCommodity(){
        commodityService.CommodityStatusIsSoldToLogicalDelete();
    }

    //每天凌晨零时十分将物品日期减一
    @Scheduled(cron = "0 10 0 * * ?")
    private void minusGoodsOutdated(){
        goodsService.minusGoodsOutdated();
    }

    //每天凌晨零时十五分将过期物品逻辑删除
    @Scheduled(cron = "0 15 0 * * ?")
    private void deleteGoodsOutdatedIs0(){
        goodsService.deleteGoodsOutdatedIs0();
    }

    //每天凌晨零时十五分将过期商品逻辑删除
    @Scheduled(cron = "0 20 0 * * ?")
    private void deleteCommodityOutdatedIs0(){
        commodityService.deleteCommodityOutdatedIs0();
    }
}
