package com.nac.abc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nac.abc.mapper.CommodityMapper;
import com.nac.abc.entity.Commodity;
import com.nac.abc.service.ICommodityService;
import org.springframework.stereotype.Service;

@Service
public class CommodityServiceImpl extends ServiceImpl<CommodityMapper, Commodity> implements ICommodityService {
}
