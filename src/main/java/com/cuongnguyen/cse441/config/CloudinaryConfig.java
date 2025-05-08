package com.cuongnguyen.cse441.config;

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
        config.put("cloud_name", "drsxhbpwp");
        config.put("api_key", "552366933472957");
        config.put("api_secret", "JXjOqMvW5-IE6BAPH9sGAF5wc68");
        config.put("secure", true);
        return new Cloudinary(config);
    }
}
