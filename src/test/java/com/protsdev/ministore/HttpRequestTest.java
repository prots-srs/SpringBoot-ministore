package com.protsdev.ministore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.protsdev.ministore.localize.LocalizeService;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

// with starting server
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private LocalizeService localizeService;

    @Test
    void openingPublicPage() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port,
                String.class))
                .contains(localizeService.getLocalizedMessage("page.mainpage.author"));
    }

}
