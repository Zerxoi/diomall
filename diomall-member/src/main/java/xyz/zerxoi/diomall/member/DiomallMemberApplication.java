package xyz.zerxoi.diomall.member;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("xyz.zerxoi.diomall.member.dao")
@EnableFeignClients(basePackages = "xyz.zerxoi.diomall.member.feign")
public class DiomallMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiomallMemberApplication.class, args);
    }

}
