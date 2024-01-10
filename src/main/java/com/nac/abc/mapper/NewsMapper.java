package com.nac.abc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nac.abc.entity.News;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NewsMapper extends BaseMapper<News> {
}
