package xyz.zerxoi.diomall.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRabbit
@SpringBootApplication
@EnableDiscoveryClient
@EnableRedisHttpSession
@MapperScan("xyz.zerxoi.diomall.order.dao")
public class DiomallOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiomallOrderApplication.class, args);
    }

}
