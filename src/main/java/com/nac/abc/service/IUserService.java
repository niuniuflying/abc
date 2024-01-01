package com.nac.abc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nac.abc.entity.User;

public interface IUserService extends IService<User> {

    User selectUserByEmail(String email);

    void setRedisCode(String email,String code);

    boolean save(User user);

    String selectCodeByEmail(String email);

    boolean updateUserInfo(User user);

}
