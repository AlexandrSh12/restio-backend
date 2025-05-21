package com.restio.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    // Test public endpoint
    @Test
    public void whenAccessPublicEndpoint_thenOk() throws Exception {
        mockMvc.perform(get("/public/greetings"))
                .andExpect(status().isOk())
                .andExpect(content().string("Greetings from a public endpoint!"));
    }

    // Test general authenticated endpoint without authentication
    @Test
    public void whenAccessUserEndpointWithoutAuth_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/api/user/info"))
                .andExpect(status().isUnauthorized());
    }

    // Test general authenticated endpoint with mock user (any authenticated user)
    @Test
    @WithMockUser // Simulates an authenticated user without specific roles for this test
    public void whenAccessUserEndpointWithAuth_thenOk() throws Exception {
        mockMvc.perform(get("/api/user/info"))
                .andExpect(status().isOk())
                .andExpect(content().string("User info endpoint: accessible to any authenticated user."));
    }

    // Test admin endpoint without authentication
    @Test
    public void whenAccessAdminEndpointWithoutAuth_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/api/admin/action"))
                .andExpect(status().isUnauthorized());
    }

    // Test admin endpoint with insufficient role (general authenticated user)
    @Test
    @WithMockUser(username = "user", roles = {"USER"}) // Simulates user with USER role
    public void whenAccessAdminEndpointWithUserRole_thenForbidden() throws Exception {
        mockMvc.perform(get("/api/admin/action"))
                .andExpect(status().isForbidden());
    }

    // Test admin endpoint with ADMIN role (using the hardcoded "admin" user's role)
    // Note: @WithMockUser uses a mock user. For HTTP Basic with our UserDetailsService,
    // we'd typically test with actual credentials. However, @WithMockUser is good for role checks.
    // To test with the actual "admin" user and "password" via HTTP Basic:
    // .with(httpBasic("admin", "password")) can be added to mockMvc.perform(...)
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void whenAccessAdminEndpointWithAdminRole_thenOk() throws Exception {
        mockMvc.perform(get("/api/admin/action"))
                .andExpect(status().isOk())
                .andExpect(content().string("Admin action endpoint: accessible only to ADMIN."));
    }

    // Example of testing with HTTP Basic credentials
    @Test
    public void whenAccessAdminEndpointWithCorrectHttpBasic_thenOk() throws Exception {
        mockMvc.perform(get("/api/admin/action")
                        .header("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString("admin:password".getBytes())))
                .andExpect(status().isOk())
                .andExpect(content().string("Admin action endpoint: accessible only to ADMIN."));
    }

    @Test
    public void whenAccessUserEndpointWithCorrectHttpBasic_thenOk() throws Exception {
        mockMvc.perform(get("/api/user/info")
                        .header("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString("admin:password".getBytes())))
                .andExpect(status().isOk())
                .andExpect(content().string("User info endpoint: accessible to any authenticated user."));
    }

    @Test
    public void whenAccessAdminEndpointWithIncorrectHttpBasic_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/api/admin/action")
                        .header("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString("admin:wrongpassword".getBytes())))
                .andExpect(status().isUnauthorized());
    }
}
