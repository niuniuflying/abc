package com.nac.abc;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTest {

/*    @Test
    public void testGen(){
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("id","001");
        hashMap.put("username","niuNiu");

        String token = JWT.create().withClaim("user",hashMap) //添加载荷
                .withExpiresAt(new Date(System.currentTimeMillis()+1000*60*60*24)) //添加过期时间(一天）
                .sign(Algorithm.HMAC256("LingChen")); //指定算法 配置密钥

        System.out.println(token);
    }*/

    /*@Test
    public void testParse(){
        String token="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9" +
                ".eyJleHAiOjE3MDQxMTAxNDcsInVzZXIiOnsiaWQiOiIwMDEiLCJ1c2VybmFtZSI6Im5pdU5pdSJ9fQ" +
                ".L-gyPOW3QR_N9ix9sg0oFe1iLDLZYdqdwhPWBw-xN20";
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("LingChen")).build();
        DecodedJWT verify = jwtVerifier.verify(token);//验证token，生成一个解析后的JWT对象
        Map<String,Claim> claim = verify.getClaims();
        System.out.println(claim.get("user"));

    }*/
}
