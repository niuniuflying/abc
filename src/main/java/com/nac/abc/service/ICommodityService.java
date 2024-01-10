package com.nac.abc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nac.abc.entity.Commodity;

import java.util.List;


public interface ICommodityService extends IService<Commodity> {
    void minusCommodityOutdated();

    void CommodityStatusIsSoldToLogicalDelete();

    void deleteCommodityOutdatedIs0();

    List<Commodity> selectCommodityByCondition(Commodity commodity);

    boolean addCommodity(Commodity commodity);

//    Commodity selectCommodityById(Integer id);

    Commodity selectOneCommodity(Commodity commodity);

    boolean updateCommodityById(Commodity commodity);

    boolean deleteCommodity(Commodity commodity);

    boolean updateCommodity(Commodity commodity);

    List<Commodity> getMyCommodity(Integer userId);

}
