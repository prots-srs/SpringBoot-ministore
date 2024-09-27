package com.protsdev.ministore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.protsdev.ministore.storage.StorageProperties;

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
    // };
    // }

}
