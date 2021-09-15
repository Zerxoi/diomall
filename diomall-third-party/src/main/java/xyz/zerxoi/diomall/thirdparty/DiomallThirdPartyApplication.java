package xyz.zerxoi.diomall.thirdparty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class DiomallThirdPartyApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiomallThirdPartyApplication.class, args);
    }

}
