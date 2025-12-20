package com.samar.Journal_app.serviceTest;

import com.samar.Journal_app.entity.User;
import com.samar.Journal_app.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;


public class RedisTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisService redisService;


    void testSendMain(){
        User user = new User();
        user.setUsername("fuddu");
        user.setPassword("duffu");
        redisService.set("first", user, 300L );
        User first = redisService.get("first", User.class);
        int a = 1;
    }
}
