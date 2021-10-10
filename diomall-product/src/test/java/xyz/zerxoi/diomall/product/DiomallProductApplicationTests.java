package xyz.zerxoi.diomall.product;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import xyz.zerxoi.diomall.product.entity.BrandEntity;
import xyz.zerxoi.diomall.product.service.BrandService;

@SpringBootTest
class DiomallProductApplicationTests {
    @Autowired
    BrandService brandService;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    RedissonClient redissonClient;

    @Test
    void datasourceTest() {
        BrandEntity entity = new BrandEntity();
        entity.setName("HUAWEI");
        System.out.println(brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 1)));
    }

    @Test
    void redisTest() {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set("hello", "world_" + UUID.randomUUID().toString());

        System.out.println(ops.get("hello"));
    }


    @Test
    void redissionClientTest() {
        System.out.println(redissonClient);
    }

}
