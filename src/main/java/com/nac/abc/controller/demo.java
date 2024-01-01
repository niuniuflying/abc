package com.nac.abc.controller;

import com.nac.abc.entity.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//测试拦截器是否成功
@RestController
@RequestMapping("/demo")
public class demo {

    @GetMapping("/login")
    public Result<String> test() {
        return new Result<>(200,"hello",null);
    }

}
