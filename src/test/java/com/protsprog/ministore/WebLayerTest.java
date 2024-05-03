package com.protsprog.ministore;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.protsprog.ministore.mainpage.MainpageController;
import com.protsprog.ministore.mainpage.MainpageService;

// test of page without starting server
@WebMvcTest // (MainpageController.class)
@Import(WebSecurityConfig.class)
// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// @AutoConfigureMockMvc
public class WebLayerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MainpageService service;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    // when(service.hi()).thenReturn("abc++");

    @Test
    void openMainpage() throws Exception {
        this.mockMvc
                .perform(get("/")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("prots.srs@gmail.com")));
    }

    @Test
    void openAdminPage() throws Exception {
        this.mockMvc
                .perform(formLogin("/login").user("serhii").password("mars13uran"))
                .andExpect(authenticated())
                .andReturn();
    }

    @Test
    void maximumSessionsTests() throws Exception {
        MvcResult mvcResult = this.mockMvc
                .perform(formLogin("/login").user("serhii").password("mars13uran"))
                .andExpect(authenticated())
                .andReturn();

        MockHttpSession firstLoginSession = (MockHttpSession) mvcResult.getRequest().getSession();

        this.mockMvc.perform(get("/admin").session(firstLoginSession))
                .andExpect(authenticated());

        this.mockMvc.perform(formLogin("/login"))
                .andExpect(unauthenticated());

        // first session is terminated by second login
        this.mockMvc.perform(get("/admin").session(firstLoginSession))
                .andExpect(authenticated());
        // .andExpect(unauthenticated());
    }
}