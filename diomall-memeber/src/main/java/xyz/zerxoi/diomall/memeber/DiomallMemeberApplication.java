package xyz.zerxoi.diomall.memeber;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("xyz.zerxoi.diomall.memeber.dao")
public class DiomallMemeberApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiomallMemeberApplication.class, args);
    }

}
