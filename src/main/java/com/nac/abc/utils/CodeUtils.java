package com.nac.abc.utils;

import java.util.Random;

public class CodeUtils {
    public static String code(){
        String all = "0123456789";//将所有验证码可能的结果列出来
        Random r = new Random();
        StringBuilder rightCode = new StringBuilder();//先定义一个空字符串，后续用于累加
        for (int i = 0; i < 6; i++) {
            int dex = r.nextInt(all.length());//随机一个索引
            rightCode.append(all.charAt(dex));
        }
        String code = String.valueOf(rightCode);
        System.out.println("CodeUtils生成的验证码："+code);
        return code;
    }

}
