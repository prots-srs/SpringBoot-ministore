package com.protsprog.ministore;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.protsprog.ministore.mainpage.MainpageService;

// test of page without starting server
@SpringBootTest
@AutoConfigureMockMvc
public class MinistoreControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MainpageService service;

    @Test
    void openingPageHi() throws Exception {

        // when(service.hi()).thenReturn("abc++");

        this.mockMvc
                .perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("prots.srs@gmail.com")));
    }
}
