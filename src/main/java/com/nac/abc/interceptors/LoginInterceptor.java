package com.nac.abc.interceptors;

import com.nac.abc.utils.JwtUtil;
import com.nac.abc.utils.ThreadLocalUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

//此类为拦截器 -> 完了之后还要注册拦截器 -> 在config下
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //令牌验证
        String token = request.getHeader("Authorization");
        //验证token
        try {
            //从Redis获取相同的token
            String redisToken = stringRedisTemplate.opsForValue().get(token);

            if(redisToken==null){
                //token已经失效
                throw new RuntimeException();
            }
            Map<String, Object> claims = JwtUtil.parseToken(token);

            //把业务数据存储到ThreadLocal中 解决每次响应都要从header里面获取并解析Token的繁琐步骤 UserInfo[①->②] 只添加这一行
            ThreadLocalUtil.set(claims);

            //放行
            return true;
        }catch (Exception e){
            response.setStatus(401);
            //不放行
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //清空ThreadLocal中的数据 防止内存泄漏
        ThreadLocalUtil.remove();
    }
}
