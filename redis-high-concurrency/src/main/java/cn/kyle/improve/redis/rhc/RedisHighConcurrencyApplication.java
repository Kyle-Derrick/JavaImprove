package cn.kyle.improve.redis.rhc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RedisHighConcurrencyApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisHighConcurrencyApplication.class, args);
    }

}
