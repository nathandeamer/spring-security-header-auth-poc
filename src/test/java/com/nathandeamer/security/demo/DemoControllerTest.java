package com.nathandeamer.security.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DemoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldPermitAllToNoAuthMethod() throws Exception {
        this.mockMvc.perform(get("/noauth"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("No Auth!")));
    }

    @Test
    public void shouldAllowAuthMethod() throws Exception {
        this.mockMvc.perform(get("/auth")
                        .header(SecurityConfig.X_PRINCIPAL_HEADER, "test")
                        .header(SecurityConfig.X_SCOPES_HEADER, "customer:read")
                )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Authed!")));
    }

    @Test
    public void shouldDenyAuthMethodWhenMissingPrincipalAndScopesHeader() throws Exception {
        this.mockMvc.perform(get("/auth"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldDenyAuthMethodWhenMissingPrincipalHeader() throws Exception {
        this.mockMvc.perform(get("/auth")
                        .header(SecurityConfig.X_SCOPES_HEADER, "customer:read")
                )
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldDenyAuthMethodWhenMissingScopesHeader() throws Exception {
        this.mockMvc.perform(get("/auth")
                        .header(SecurityConfig.X_SCOPES_HEADER, "customer:read")
                )
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldDenyAuthMethodWhenIncorrectScopesHeaderValue() throws Exception {
        this.mockMvc.perform(get("/auth")
                        .header(SecurityConfig.X_PRINCIPAL_HEADER, "test")
                        .header(SecurityConfig.X_SCOPES_HEADER, "customer:write")
                )
                .andExpect(status().isForbidden());
    }

}