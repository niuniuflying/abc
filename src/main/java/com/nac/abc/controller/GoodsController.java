package com.nac.abc.controller;

import com.alibaba.fastjson2.JSON;
import com.nac.abc.entity.Goods;
import com.nac.abc.entity.Result;
import com.nac.abc.service.impl.GoodsServiceImpl;
import com.nac.abc.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsServiceImpl goodsService;

    @PostMapping("/addGoods")
    public Result<String> addGoods(@RequestBody Goods goods){
        Map<String,Object> stringObjectMap = ThreadLocalUtil.get();
        Integer userId = (Integer) stringObjectMap.get("id");
        goods.setUserId(userId);
        boolean addGoods = goodsService.addGoods(goods);
        if (addGoods){
            return new Result<>(200,"添加失物招领成功",null);
        }else {
            return new Result<>(500,"添加失物招领失败",null);
        }
    }

    @PutMapping("/deleteGoods")
    public Result<String> deleteGoods(@RequestBody Goods goods){
        boolean deleteGoods = goodsService.deleteGoods(goods);
        if (deleteGoods){
            return new Result<>(200,"删除失物招领成功",null);
        }else {
            return new Result<>(500,"删除失物招领失败",null);
        }
    }

    @PutMapping("/updateGoods")
    public Result<String> updateGoods(@RequestBody Goods goods){
        boolean updateGoods = goodsService.updateGoods(goods);
        if (updateGoods){
            return new Result<>(200,"修改失物招领成功",null);
        }else {
            return new Result<>(500,"修改失物招领失败",null);
        }
    }

    @GetMapping("/getGoodsByConditionWithPage")
    public Result<List<Goods>> getGoodsByConditionWithPage(@RequestBody Map<String,Object> stringObjectMap){
        String s = JSON.toJSONString(stringObjectMap);
        Goods goods = JSON.parseObject(s, Goods.class);

        // 获取 current 和 size，并转换为 Integer
        Integer current = stringObjectMap.get("current") != null ? Integer.parseInt(stringObjectMap.get("current").toString()) : null;
        Integer size = stringObjectMap.get("size") != null ? Integer.parseInt(stringObjectMap.get("size").toString()) : null;

        String startTime = String.valueOf(stringObjectMap.get("startTime"));
        String endTime = String.valueOf(stringObjectMap.get("endTime"));

        List<Goods> goodsByCondition = goodsService.getGoodsByConditionWithPage(goods,startTime,endTime,current,size);
        return new Result<>(200,"条件查找成功",goodsByCondition);
    }
}

