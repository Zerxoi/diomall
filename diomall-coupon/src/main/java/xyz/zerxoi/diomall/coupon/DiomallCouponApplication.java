package xyz.zerxoi.diomall.coupon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("xyz.zerxoi.diomall.coupon.dao")
public class DiomallCouponApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiomallCouponApplication.class, args);
    }

}
