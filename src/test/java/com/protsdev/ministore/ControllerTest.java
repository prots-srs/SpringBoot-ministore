package com.protsdev.ministore;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.protsdev.ministore.localize.LocalizeService;

// test of page without starting server
@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTest {

    @Value("${user.admin.name}")
    private String adminName;

    @Value("${user.admin.password}")
    private String adminPassword;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LocalizeService localizeService;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    void openingPublicPage() throws Exception {

        mockMvc
                .perform(get("/"))
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(
                        content().string(containsString(localizeService.getMessage("page.mainpage.author"))));
    }

    @Test
    void openPanelPage() throws Exception {
        mockMvc
                .perform(formLogin("/login").user(adminName).password(adminPassword))
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
