package com.example.bookshop.global.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {


    private final RedisTemplate<String, String> redisTemplate;


    public void setDataExpireMinutes(String key, String value, Long expiredTime) {

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        valueOperations.set(key, value, Duration.ofMinutes(expiredTime));

    }

    public void setDataExpireMillis(String key, String value, Long expiredTime) {

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        valueOperations.set(key, value, Duration.ofMillis(expiredTime));

    }



    public String getData(String key) {

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        return valueOperations.get(key);

    }

    public void deleteData(String key) {

        redisTemplate.delete(key);

    }

    public Long getExpire(String key, TimeUnit milliseconds) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        Long expire = valueOperations.getOperations().getExpire(key, TimeUnit.MILLISECONDS);

        return expire;
    }

}
