package com.nac.abc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nac.abc.mapper.UserMapper;
import com.nac.abc.entity.User;
import com.nac.abc.service.IUserService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void setRedisCode(String email,String code) {
        stringRedisTemplate.opsForHash().put(email,"code",code);
        stringRedisTemplate.expire(email, 5, TimeUnit.MINUTES);
    }

    @Override
    public boolean save(User user) {
        int insert = userMapper.insert(user);
        return insert == 1;
    }

    @Override
    public String selectCodeByEmail(String email) {
        return Objects.requireNonNull(stringRedisTemplate.opsForHash().get(email, "code")).toString();
    }

    @Override
    public boolean updateUserInfo(User user) {
        int updateById = userMapper.updateById(user);
        return updateById == 1;
    }

    @Override
    public List<User> selectAllUserExceptAdmin() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //只查询普通用户
        queryWrapper.eq("isAdmin","0");
        return userMapper.selectList(queryWrapper);
    }

    //管理员删除用户
    @Override
    public boolean adminDeleteUser(User user) {
        user.setIsDelete("1");
        int updateById = userMapper.updateById(user);
        return updateById == 1;
    }

    @Override
    public boolean adminUpdateUserInfo(User user) {
        int updateById = userMapper.updateById(user);
        return updateById == 1;
    }

    @Override
    public List<User> getUserByCondition(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Strings.isNotEmpty(user.getUsername()),User::getUsername,user.getUsername());
        wrapper.like(Strings.isNotEmpty(user.getEmail()),User::getEmail,user.getEmail());
        wrapper.eq(Strings.isNotEmpty(user.getIsAdmin()),User::getIsAdmin,user.getIsAdmin());
        wrapper.eq(Strings.isNotEmpty(user.getIsDelete()),User::getIsDelete,user.getIsDelete());
        return userMapper.selectList(wrapper);
    }

    @Override
    public User selectUserByEmail(String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email",email);
        return userMapper.selectOne(queryWrapper);
    }
}
