package cn.kyle.improve.redis.rhc.controller.v2;

import cn.kyle.improve.redis.rhc.constant.RedisKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/stock/v2")
public class V2StockController {
    private final RedisTemplate<String, String> redisTemplate;

    @GetMapping("/deduct_stock")
    public synchronized String deductStock() {
        boolean lock = false;
        try {
            // 尝试获取锁， 并设置锁超时
            // 存在超时问题，不加超时会有程序崩掉导致锁未释放问题
            lock = Boolean.TRUE.equals(redisTemplate.opsForValue()
                    .setIfAbsent(RedisKey.stock_lock.name(), "1", 10, TimeUnit.SECONDS));
            if (!lock) {
                log.warn("系统繁忙");
                return "系统繁忙";
            }
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
        }  finally {
            if (lock) {
                redisTemplate.delete(RedisKey.stock_lock.name());
            }
        }
    }
}
