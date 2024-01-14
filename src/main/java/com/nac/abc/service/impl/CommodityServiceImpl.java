package com.nac.abc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nac.abc.entity.User;
import com.nac.abc.mapper.CommodityMapper;
import com.nac.abc.entity.Commodity;
import com.nac.abc.service.ICommodityService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class CommodityServiceImpl extends ServiceImpl<CommodityMapper, Commodity> implements ICommodityService {

    @Autowired
    private CommodityMapper commodityMapper;
    @Override
    public void minusCommodityOutdated() {
            List<Commodity> commodityList = commodityMapper.selectList(null);

            commodityList.forEach(commodity -> commodity.setOutdated(commodity.getOutdated() - 1));
    }

    @Override
    public void CommodityStatusIsSoldToLogicalDelete() {
        QueryWrapper<Commodity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status","1");
        List<Commodity> commodities = commodityMapper.selectList(queryWrapper);
        commodities.forEach(commodity -> commodity.setIsDelete("1"));
    }

    @Override
    public void deleteCommodityOutdatedIs0() {
        QueryWrapper<Commodity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("outdated",0);
        List<Commodity> commodityList = commodityMapper.selectList(queryWrapper);
        commodityList.forEach(commodity -> commodity.setIsDelete("1"));
    }

    @Override
    public List<Commodity> selectCommodityByCondition(Commodity commodity) {
        commodity.setIsDelete("0");
        LambdaQueryWrapper<Commodity> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Strings.isNotEmpty(commodity.getName()),Commodity::getName,commodity.getName());
        wrapper.like(Strings.isNotEmpty(commodity.getType()),Commodity::getType,commodity.getType());
        wrapper.like(Strings.isNotEmpty(commodity.getDegree()),Commodity::getDegree,commodity.getDegree());
        wrapper.like(Strings.isNotEmpty(commodity.getIsDelete()),Commodity::getIsDelete,commodity.getIsDelete());
        List<Commodity> commodityList = commodityMapper.selectList(wrapper);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        commodityList.sort(Comparator.comparing(commodities ->
                LocalDateTime.parse(commodities.getUpdateTime(), formatter)));
        Collections.reverse(commodityList);
        return commodityList;
    }

    @Override
    public boolean addCommodity(Commodity commodity) {
        int insert = commodityMapper.insert(commodity);
        return insert == 1;
    }

    @Override
    public Commodity selectOneCommodity(Commodity commodity) {
        QueryWrapper<Commodity> wrapper = new QueryWrapper<>();
        wrapper.eq("name",commodity.getName())
                        .eq("price",commodity.getPrice())
                                .eq("degree",commodity.getDegree())
                                        .eq("description",commodity.getDescription())
                                                .eq("type",commodity.getType());
        return commodityMapper.selectOne(wrapper);
    }

    @Override
    public boolean updateCommodityById(Commodity commodity) {
        int updateById = commodityMapper.updateById(commodity);
        return updateById==1;
    }

    @Override
    public boolean deleteCommodity(Commodity commodity) {
        commodity.setIsDelete("1");
        int updateById = commodityMapper.updateById(commodity);
        return updateById==1;
    }

    @Override
    public boolean updateCommodity(Commodity commodity) {
        int updateById = commodityMapper.updateById(commodity);
        return updateById==1;
    }

    @Override
    public List<Commodity> getMyCommodity(Integer userId) {
        QueryWrapper<Commodity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId",userId);
        return commodityMapper.selectList(queryWrapper);
    }

    @Override
    public Commodity selectOneById(Integer id) {
        return commodityMapper.selectById(id);
    }

    /*@Override
    public Commodity selectCommodityById(Integer id) {
        return commodityMapper.selectById(id);
    }*/
}
