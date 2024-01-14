package com.nac.abc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

public class MongoDBTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void findAll(){
    }

}
