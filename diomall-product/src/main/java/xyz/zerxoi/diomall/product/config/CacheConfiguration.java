package xyz.zerxoi.diomall.product.config;

import java.time.Duration;

import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.cache.CacheProperties.Redis;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author zouxin <zouxin@kuaishou.com>
 * Created on 2021-10-10
 */
@Configuration
@EnableCaching
public class CacheConfiguration {
    @Bean
    public RedisCacheConfiguration redisCacheConfiguration(CacheProperties cacheProperties ) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        // 自定义 value 序列化器
        config = config.serializeValuesWith(SerializationPair.fromSerializer(RedisSerializer.json()));

        // 从配置文件获取配置
        Redis redisProperties = cacheProperties.getRedis();
        if (redisProperties.getTimeToLive() != null) {
            config = config.entryTtl(redisProperties.getTimeToLive());
        }
        if (redisProperties.getKeyPrefix() != null) {
            config = config.prefixCacheNameWith(redisProperties.getKeyPrefix());
        }
        if (!redisProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }
        if (!redisProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }
        return config;
    }
}
