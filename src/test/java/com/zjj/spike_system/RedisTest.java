package com.zjj.spike_system;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private DefaultRedisScript script;

    @Test
    public void test1(){
        ValueOperations opsForValue = redisTemplate.opsForValue();
        // 获取锁，并指定过期时间
        // 生成锁的唯一值
        String val = UUID.randomUUID().toString().replace("-", "");
        Boolean isLock = opsForValue.setIfAbsent("lock", val,5, TimeUnit.SECONDS);
        if (isLock){
            // ......
            // 执行lua脚本
            Boolean result = (Boolean)redisTemplate.execute(script, Collections.singletonList("lock"), val);
            System.out.println(result);
        }else{
            // 获取锁失败
            System.out.println("被占用");
        }
    }

}
