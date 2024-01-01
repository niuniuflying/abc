package com.nac.abc.utils;

import java.util.UUID;

public class UUIDUtil {

    /**
     * 生成随机UUID（32个字符）
     *
     * @return 随机UUID字符串
     */
    public static String generateRandomUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成指定数量的随机UUID
     *
     * @param count 要生成的UUID数量
     * @return 包含指定数量随机UUID的数组
     */
    public static String[] generateRandomUUIDs(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Count should be greater than zero.");
        }

        String[] uuids = new String[count];
        for (int i = 0; i < count; i++) {
            uuids[i] = generateRandomUUID();
        }
        return uuids;
    }

    // 可以添加其他UUID相关的方法，根据需要进行扩展
}
