package com.friendhub.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourcesWebConfig implements WebMvcConfigurer {

    @Value("${upload-file.base-uri}")
    private String basePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String path = basePath.replace("\\", "/");

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + path + "/");
    }
}
