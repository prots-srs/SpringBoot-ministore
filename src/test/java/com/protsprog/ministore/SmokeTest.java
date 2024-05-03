package com.protsprog.ministore;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.protsprog.ministore.mainpage.MainpageController;

@SpringBootTest
public class SmokeTest {

    @Autowired
    private MainpageController hiController;

    @Test
    void contextLoads() throws Exception {
        assertThat(hiController).isNotNull();
    }
}
