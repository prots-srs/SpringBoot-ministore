package com.protsprog.ministore;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    public void addViewControllers(@SuppressWarnings("null") ViewControllerRegistry registry) {
        // registry.addViewController("/").setViewName("mainpage");
        // registry.addViewController("/maintenance").setViewName("dashboard");
        registry.addViewController("/login").setViewName("login");
    }

}
