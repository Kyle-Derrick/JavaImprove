package cn.kyle.improve.redis.rhc.controller.v1;

import cn.kyle.improve.redis.rhc.constant.RedisKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/stock/v1")
public class V1StockController {
    private final RedisTemplate<String, String> redisTemplate;

    @GetMapping("/deduct_stock")
    public synchronized String deductStock() {
        // 多服务器时存在分布式锁问题
        String value = redisTemplate.opsForValue().get(RedisKey.stock.name());
        if (value == null) {
            return "无库存";
        }
        long stock = Long.parseLong(value);
        if (stock > 0) {
            long realStock = stock - 1;
            redisTemplate.opsForValue().set(RedisKey.stock.name(), String.valueOf(realStock));
            log.info("扣减成功, 剩余库存：{}", realStock);
            return "扣减成功, 剩余库存：" + realStock;
        } else {
            log.info("扣减失败, 库存不足");
            return "扣减失败, 库存不足";
        }
    }
}
