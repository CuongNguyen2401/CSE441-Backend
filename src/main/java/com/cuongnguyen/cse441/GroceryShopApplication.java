package com.cuongnguyen.cse441;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GroceryShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(GroceryShopApplication.class, args);
    }

}
