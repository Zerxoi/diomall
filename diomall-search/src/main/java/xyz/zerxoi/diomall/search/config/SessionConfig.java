package xyz.zerxoi.diomall.search.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

// 一个服务设置，其他服务都要设置
@Configuration
public class SessionConfig {
    // 使用 CookieSerializer 将Session域设置为父域
    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("DIOSESSION");
        serializer.setDomainName("diomall.com");
        return serializer;
    }

    // 设置 Redis 序列化器
    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }
}
