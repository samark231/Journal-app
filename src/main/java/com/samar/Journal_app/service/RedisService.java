package com.samar.Journal_app.service;
import java.lang.Object;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samar.Journal_app.dto.WeatherApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RedisService {


    @Autowired
    private RedisTemplate redisTemplate;

    public <T> T get(String key, Class<T> weatherApiResponseClass){
        try{
            Object object = redisTemplate.opsForValue().get(key);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(object.toString(), weatherApiResponseClass);
        } catch (Exception e) {
            log.error(" error while fetching data from redis: ", e);
            return  null;
        }
    }

    public void set(String key, Object o, Long ttl){
        try{
            ObjectMapper mapper = new ObjectMapper();
            String jsonValue = mapper.writeValueAsString(o);
            redisTemplate.opsForValue().set(key, jsonValue, ttl, TimeUnit.SECONDS);
        }catch (Exception e){
            log.error("Error while writing data into redis: ", e);
        }
    }
}
