package com.example.bookshop.global.service;


import com.example.bookshop.global.exception.CustomException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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


    private final ObjectMapper objectMapper;

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

    public <T> void setInfoData(String key, T value, long expiredTime) {

        try {

            String jsonData = objectMapper.writeValueAsString(value);

            redisTemplate.opsForValue().set(key, jsonData, Duration.ofMillis(expiredTime));


        } catch (JsonProcessingException e) {
            e.getMessage();
        }

    }

    public <T> T getInfoData(String key, Class<T> clazz) {

        String jsonData = redisTemplate.opsForValue().get(key);

        if (jsonData == null) {

            return null;
        }

        try {

            return objectMapper.readValue(jsonData, clazz);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Redis에서 객체 변환 중 오류");
        }


    }

}
