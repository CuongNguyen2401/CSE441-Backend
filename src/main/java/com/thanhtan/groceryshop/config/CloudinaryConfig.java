package com.thanhtan.groceryshop.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary getCloudinary(){

        Map config = new HashMap();
        config.put("cloud_name", "db9wedddz");
        config.put("api_key", "341194475366122");
        config.put("api_secret", "US4dUOYUnM98KokEy-kSD1-ybjY");
        config.put("secure", true);
        return new Cloudinary(config);
    }
}
