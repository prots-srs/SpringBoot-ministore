package com.protsdev.ministore;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

import com.protsdev.ministore.localize.LocalizeService;
import com.protsdev.ministore.storage.StorageProperties;
import com.protsdev.ministore.storage.StorageService;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class MinistoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(MinistoreApplication.class, args);
    }

    // @Profile("h2")
    // @Bean
    // @Order(1)
    // CommandLineRunner deleteSavedFiles(StorageService storageService,
    // LocalizeService localizeService) {
    // return args -> {
    // storageService.deleteAll();
    // };
    // }

    @Bean
    // @Order(2)
    CommandLineRunner init(StorageService storageService, LocalizeService localizeService) {
        return args -> {
            storageService.init();
        };
    }

}
