package com.protsprog.ministore;

import java.time.Duration;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
// @EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

    public void addViewControllers(@SuppressWarnings("null") ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/error").setViewName("error");
    }

    // @Override
    // public void addResourceHandlers(ResourceHandlerRegistry registry) {
    // registry.addResourceHandler("/resources/**")
    // .addResourceLocations("/public/content", "classpath:/static/");
    // .setCacheControl(CacheControl.maxAge(Duration.ofDays(365)));
    // }
    // System.getProperty("user.dir")

}
