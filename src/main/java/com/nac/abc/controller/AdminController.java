package com.nac.abc.controller;

import com.alibaba.fastjson2.JSON;
import com.nac.abc.entity.Result;
import com.nac.abc.entity.User;
import com.nac.abc.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private IUserService iUserService;

    //管理员查询所有用户不包括管理员且不展示头像 密码
    @GetMapping("/selectAllUserExceptAdmin")
    public Result<List<User>> selectAllUserExceptAdmin(){
        return new Result<>(200,"查询所有用户信息除管理员",iUserService.selectAllUserExceptAdmin());
    }

    //管理员删除普通用户
    @PutMapping("/deleteUser")
    //stringObjectMap 必须含有需删除用户的id
    public Result<String> adminDeleteUser(@RequestBody Map<String,Object> stringObjectMap){
        String string = JSON.toJSONString(stringObjectMap);
        User user = JSON.parseObject(string, User.class);
        boolean deleteUser = iUserService.adminDeleteUser(user);
        if (deleteUser){
            return  new Result<>(200,"删除普通用户成功",null);
        }else {
            return  new Result<>(500,"删除普通用户失败",null);
        }
    }

    //管理员修改用户信息 传入一个User 根据user中的id更新数据 不能更新用户密码
    @PutMapping("/updateUserInfo")
    public Result<String> adminUpdateUserInfo(@RequestBody Map<String,Object> stringObjectMap){
        String string = JSON.toJSONString(stringObjectMap);
        User user = JSON.parseObject(string, User.class);
        boolean adminUpdateUserInfo = iUserService.adminUpdateUserInfo(user);
        if (adminUpdateUserInfo){
            return  new Result<>(200,"修改用户信息成功",null);
        }else {
            return  new Result<>(500,"修改用户信息失败",null);
        }
    }

    //条件查询用户信息 用户名 邮箱 是否注销 不能查询管理员
    @PostMapping("/getUserByCondition")
    public Result<List<User>> getUserByCondition(@RequestBody Map<String,Object> stringObjectMap){
        System.out.println(stringObjectMap);
        String string = JSON.toJSONString(stringObjectMap);
        User user = JSON.parseObject(string, User.class);
        user.setIsAdmin("0");
        List<User> userByCondition = iUserService.getUserByCondition(user);
        System.out.println(userByCondition);
        return new Result<>(200,"查询成功",userByCondition);
    }

}
