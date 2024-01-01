package com.nac.abc.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nac.abc.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}