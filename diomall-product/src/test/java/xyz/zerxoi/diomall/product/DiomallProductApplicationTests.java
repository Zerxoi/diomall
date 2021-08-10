package xyz.zerxoi.diomall.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import xyz.zerxoi.diomall.product.entity.BrandEntity;
import xyz.zerxoi.diomall.product.service.BrandService;

@SpringBootTest
class DiomallProductApplicationTests {
    @Autowired
    BrandService brandService;

    @Test
    void contextLoads() {
        BrandEntity entity = new BrandEntity();
        entity.setName("HUAWEI");
        System.out.println(brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 1)));
    }

}
