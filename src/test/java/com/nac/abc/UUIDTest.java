package com.nac.abc;

import com.nac.abc.utils.UUIDUtil;
import org.junit.jupiter.api.Test;

public class UUIDTest {

    @Test
    public void generateUUID(){
        System.out.println(UUIDUtil.generateRandomUUID());
    }
}
