package cn.kyle.improve.redis.rhc.config;

import cn.kyle.improve.redis.rhc.constant.RedisKey;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@RequiredArgsConstructor
public class StockConfiguration {
    private final RedisTemplate<String, String> redisTemplate;

    @PostConstruct
    public void init() {
        String value = redisTemplate.opsForValue().get(RedisKey.stock.name());
        if (value == null || Long.parseLong(value) <= 0) {
            redisTemplate.opsForValue().set(RedisKey.stock.name(), String.valueOf(1000));
        }
    }
}
