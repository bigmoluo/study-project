package com.example.studyprojectbacked.util;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class FlowUtil {
    @Resource
    StringRedisTemplate stringRedisTemplate;

    public boolean limitOnceCheck(String key, int blockTime){
        if(Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))){
            return false;
        } else {
            stringRedisTemplate.opsForValue().set(key, "", blockTime, TimeUnit.SECONDS);
            return true;
        }
    }

    /**
     * 针对于时间段内多次请求，如3秒内20次请求
     * @param counterKey 计数器
     * @param frequency 请求频率
     * @param period 计数周期
     * @return 是否通过限流检查
     */
    public boolean limitPeriodCounterCheck(String counterKey, int frequency, int period){
        return this.internalCheck(counterKey, frequency, period, overclock -> !overclock);
    }

    /**
     * 内部使用请求限制主要逻辑
     * @param key 计数键
     * @param frequency 请求频率
     * @param period 计数周期
     * @param action 限制行为与策略
     * @return 是否通过限流检查
     */
    private boolean internalCheck(String key, int frequency, int period, LimitAction action){
        String count = stringRedisTemplate.opsForValue().get(key);
        if (count != null) {
            long value = Optional.ofNullable(stringRedisTemplate.opsForValue().increment(key)).orElse(0L);
            int c = Integer.parseInt(count);
            if(value != c + 1)
                stringRedisTemplate.expire(key, period, TimeUnit.SECONDS);
            return action.run(value > frequency);
        } else {
            stringRedisTemplate.opsForValue().set(key, "1", period, TimeUnit.SECONDS);
            return true;
        }
    }

    /**
     * 内部使用，限制行为与策略
     */
    private interface LimitAction {
        boolean run(boolean overclock);
    }
}
