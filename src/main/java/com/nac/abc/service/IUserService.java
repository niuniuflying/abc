package com.nac.abc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nac.abc.entity.User;

import java.util.List;

public interface IUserService extends IService<User> {

    User selectUserByEmail(String email);

    void setRedisCode(String email,String code);

    boolean save(User user);

    String selectCodeByEmail(String email);

    boolean updateUserInfo(User user);

    List<User> selectAllUserExceptAdmin();

    boolean adminDeleteUser(User user);

    boolean adminUpdateUserInfo(User user);

    List<User> getUserByCondition(User user);

}
