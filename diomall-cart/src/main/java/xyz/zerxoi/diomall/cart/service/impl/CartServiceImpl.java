package xyz.zerxoi.diomall.cart.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import xyz.zerxoi.diomall.cart.service.CartService;

@Slf4j
@Service
public class CartServiceImpl implements CartService {
    final StringRedisTemplate redisTemplate;

    public CartServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
