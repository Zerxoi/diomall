package xyz.zerxoi.diomall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("xyz.zerxoi.diomall.product.dao")
public class DiomallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiomallProductApplication.class, args);
    }

}
