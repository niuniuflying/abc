package com.nac.abc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nac.abc.entity.Goods;

import java.time.LocalDateTime;
import java.util.List;

public interface IGoodsService extends IService<Goods> {
    void minusGoodsOutdated();

    void deleteGoodsOutdatedIs0();

    boolean addGoods(Goods goods);

    boolean deleteGoods(Goods goods);

    boolean updateGoods(Goods goods);

//    List<Goods> getGoodsByCondition(Goods goods);
    List<Goods> getGoodsByConditionWithPage(Goods goods, String startTime, String endTime, Integer current, Integer size);
}
