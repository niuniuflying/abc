package com.nac.abc.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nac.abc.entity.Goods;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {
}