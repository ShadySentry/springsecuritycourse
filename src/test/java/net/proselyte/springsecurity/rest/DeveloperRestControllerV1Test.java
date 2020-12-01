package net.proselyte.springsecurity.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.proselyte.springsecurity.SpringSecurityApplication;
import net.proselyte.springsecurity.security.JwtTokenFilter;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import java.io.UnsupportedEncodingException;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = SpringSecurityApplication.class)
class DeveloperRestControllerV1Test {

    @Autowired
    WebApplicationContext wac;

    @Autowired
//    FilterChainProxy springSecurityFilterChain;
    JwtTokenFilter springSecurityFilterChain;
    private MockMvc mockMvc;

    private static final String CLIENT_ID = "fooClientIdPassword";
//    private static final String CLIENT_SECRET = "proselite";

    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";

    private static final String EMAIL = "jim@yahoo.com";
    private static final String NAME = "Jim";

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(springSecurityFilterChain)
                .build();
    }

    private String obtainAccessToken(String email, String password) throws Exception {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        AuthenticationRequestDto adminRequestDto = new AuthenticationRequestDto();
        adminRequestDto.setEmail(email);
        adminRequestDto.setPassword(password);

        ResultActions result = mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(adminRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));


        String resultString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("token").toString();
    }


    @Test
    void getAll() throws Exception {
        final String accessToken = obtainAccessToken("admin@email.com", "admin");
        System.out.println("token:" + accessToken);

        MvcResult result= mockMvc.perform(get("/api/v1/developers")
                .header("Authorization",accessToken))
                .andExpect(status().isOk()).andExpect(jsonPath("$.content[0].id",is(1))).andReturn();
    }
}