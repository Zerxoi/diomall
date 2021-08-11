package xyz.zerxoi.diomall.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("xyz.zerxoi.diomall.order.dao")
public class DiomallOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiomallOrderApplication.class, args);
    }

}
