package xyz.zerxoi.diomall.product.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zouxin <zouxin@kuaishou.com>
 * Created on 2021-10-07
 */
@Configuration
public class RedissonConfig {

    // TODO 实际上并没有用到 Redisson
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        return Redisson.create(config);
    }
}
