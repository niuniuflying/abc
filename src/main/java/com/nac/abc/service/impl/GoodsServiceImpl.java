package com.nac.abc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nac.abc.mapper.GoodsMapper;
import com.nac.abc.entity.Goods;
import com.nac.abc.service.IGoodsService;
import org.springframework.stereotype.Service;

@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {
}
