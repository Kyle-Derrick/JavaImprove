package cn.kyle.improve.redis.rhc.controller;

import cn.kyle.improve.redis.rhc.constant.RedisCacheKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
public class StockController {
    private final ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;

    public StockController(ReactiveRedisTemplate<String, Object> reactiveRedisTemplate) {
        this.reactiveRedisTemplate = reactiveRedisTemplate;
    }

    @GetMapping
    public Mono<String> deductStock() {
        reactiveRedisTemplate.opsForValue().get(RedisCacheKey.stock.name())
            .subscribe(i -> {
                i.toString()
            })
    }
}
