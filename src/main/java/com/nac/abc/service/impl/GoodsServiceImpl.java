package com.nac.abc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nac.abc.mapper.GoodsMapper;
import com.nac.abc.entity.Goods;
import com.nac.abc.service.IGoodsService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public void minusGoodsOutdated() {
        List<Goods> goodsList = goodsMapper.selectList(null);
        goodsList.forEach(goods -> goods.setOutdated(goods.getOutdated()-1));
    }

    @Override
    public void deleteGoodsOutdatedIs0() {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("outdated",0);
        List<Goods> goodsList = goodsMapper.selectList(queryWrapper);
        goodsList.forEach(goods -> goods.setIsDelete("1"));
    }

    @Override
    public boolean addGoods(Goods goods) {
        int insert = goodsMapper.insert(goods);
        return insert==1;
    }

    @Override
    public boolean deleteGoods(Goods goods) {
        goods.setIsDelete("1");
        int updateById = goodsMapper.updateById(goods);
        return updateById==1;
    }

    @Override
    public boolean updateGoods(Goods goods) {
        int updateById = goodsMapper.updateById(goods);
        return updateById==1;
    }

    /* @Override
    *public List<Goods> getGoodsByCondition(Goods goods) {
         goods.setIsDelete("1");
         LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
         wrapper.like(Strings.isNotEmpty(goods.getName()),Goods::getName,goods.getName());
         wrapper.like(Strings.isNotEmpty(goods.getTime()),Goods::getTime,goods.getTime());
         wrapper.like(Strings.isNotEmpty(goods.getDescription()),Goods::getDescription,goods.getDescription());
         wrapper.like(Strings.isNotEmpty(goods.getStatus()),Goods::getStatus,goods.getStatus());
         wrapper.eq(Strings.isNotEmpty(goods.getIsDelete()),Goods::getIsDelete,goods.getIsDelete());
         List<Goods> goodsList = goodsMapper.selectList(wrapper);
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
         goodsList.sort(Comparator.comparing(g ->
                 LocalDateTime.parse(g.getUpdateTime(), formatter)));
         Collections.reverse(goodsList);
         return goodsList;
     }
    @Override
    public List<Goods> getGoodsByConditionWithPage(Goods goods, Integer current, Integer size) {
        goods.setIsDelete("0");
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Strings.isNotEmpty(goods.getName()), Goods::getName, goods.getName());
        wrapper.like(Strings.isNotEmpty(goods.getTime()), Goods::getTime, goods.getTime());
        wrapper.like(Strings.isNotEmpty(goods.getDescription()), Goods::getDescription, goods.getDescription());
        wrapper.like(Strings.isNotEmpty(goods.getStatus()), Goods::getStatus, goods.getStatus());
        wrapper.eq(Strings.isNotEmpty(goods.getIsDelete()), Goods::getIsDelete, goods.getIsDelete());

        // 构建分页对象  需要用到MPConfig
        Page<Goods> page = new Page<>(current, size);

        // 执行分页查询
        List<Goods> goodsList = goodsMapper.selectPage(page, wrapper).getRecords();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        goodsList.sort(Comparator.comparing(g -> LocalDateTime.parse(g.getUpdateTime(), formatter)));
        Collections.reverse(goodsList);

        return goodsList;
    }*/
    @Override
    public List<Goods> getGoodsByConditionWithPage(Goods goods, String startTime, String endTime, Integer current, Integer size) {
        goods.setIsDelete("0");
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Strings.isNotEmpty(goods.getName()), Goods::getName, goods.getName());
//        wrapper.like(Strings.isNotEmpty(goods.getTime()), Goods::getTime, goods.getTime());
        wrapper.like(Strings.isNotEmpty(goods.getDescription()), Goods::getDescription, goods.getDescription());
        wrapper.like(Strings.isNotEmpty(goods.getStatus()), Goods::getStatus, goods.getStatus());
        wrapper.eq(Strings.isNotEmpty(goods.getIsDelete()), Goods::getIsDelete, goods.getIsDelete());

        if (startTime!=null&&endTime!=null){
            wrapper.between(Goods::getTime,startTime,endTime);
        }

        // 执行查询但不分页
        List<Goods> goodsList = goodsMapper.selectList(wrapper);

        // 按时间倒序排序
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        goodsList.sort(Comparator.comparing(g -> LocalDateTime.parse(g.getUpdateTime(), formatter), Comparator.reverseOrder()));

        // 计算分页起始索引
        int startIndex = (current - 1) * size;
        int endIndex = Math.min(startIndex + size, goodsList.size());

        // 截取分页范围

        return goodsList.subList(startIndex, endIndex);
    }

}
