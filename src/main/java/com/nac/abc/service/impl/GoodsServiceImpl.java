package com.nac.abc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nac.abc.mapper.GoodsMapper;
import com.nac.abc.entity.Goods;
import com.nac.abc.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
