package xyz.zerxoi.diomall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("xyz.zerxoi.diomall.product.dao")
public class DiomallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiomallProductApplication.class, args);
    }

}
