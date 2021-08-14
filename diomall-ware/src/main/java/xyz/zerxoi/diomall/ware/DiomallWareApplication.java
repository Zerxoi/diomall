package xyz.zerxoi.diomall.ware;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("xyz.zerxoi.diomall.ware.dao")
public class DiomallWareApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiomallWareApplication.class, args);
    }

}
