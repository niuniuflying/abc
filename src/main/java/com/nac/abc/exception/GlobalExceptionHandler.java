package com.nac.abc.exception;

import com.nac.abc.entity.Result;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//处理全局异常 规范返回前端数据格式  heima视频注册校验时提到
@RestControllerAdvice
public class GlobalExceptionHandler {
    public Result<String> handleException(Exception e){
        e.printStackTrace();
        return new Result<>(500,"异常", StringUtils.hasLength(e.getMessage())?e.getMessage() : "异常");
    }
}
