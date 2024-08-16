package com.protsdev.ministore;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.protsdev.ministore.localize.LocalizeService;
import com.protsdev.ministore.storage.StorageProperties;
import com.protsdev.ministore.storage.StorageService;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class MinistoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(MinistoreApplication.class, args);
    }

    @Bean
    CommandLineRunner init(StorageService storageService, LocalizeService localizeService) {
        return args -> {
            // storageService.deleteAll();
            storageService.init();
        };
    }

}
