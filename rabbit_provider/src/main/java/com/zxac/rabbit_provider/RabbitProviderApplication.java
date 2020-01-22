package com.zxac.rabbit_provider;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "com.zxac.rabbit_provider.mapper")
public class RabbitProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitProviderApplication.class, args);
    }

}
