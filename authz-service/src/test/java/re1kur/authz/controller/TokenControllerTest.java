package re1kur.authz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import re1kur.authz.service.TokenService;
import re1kur.core.dto.JwtToken;
import re1kur.core.exception.EndpointNotFoundException;
import re1kur.core.exception.InvalidTokenException;
import re1kur.core.exception.TokenDidNotPassVerificationException;
import re1kur.core.exception.UserDoesNotHavePermissionForEndpoint;
import re1kur.core.payload.RefreshTokenPayload;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TokenController.class)
class TokenControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private TokenService service;

    private final String URL = "/api/token/refresh";

    private JwtToken jwtToken;

    @BeforeEach
    void setUp() {
        jwtToken = new JwtToken("access-token", "refresh-token",
                LocalDateTime.now().plusMinutes(5), LocalDateTime.now().plusMinutes(10));
    }

    @Test
    void refreshToken__Success__ReturnsOk() throws Exception {
        RefreshTokenPayload payload = new RefreshTokenPayload("some-refresh-token");

        when(service.refreshToken(payload.refreshToken())).thenReturn(jwtToken);

        mvc.perform(put(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(jwtToken)));

        verify(service, times(1)).refreshToken(payload.refreshToken());
    }

    @Test
    void refreshToken__InvalidToken__ReturnsBadRequest() throws Exception {
        when(service.refreshToken(anyString())).thenThrow(new InvalidTokenException("Invalid token"));

        mvc.perform(put(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"refreshToken": "bad-token"}
                            """))
                .andExpect(status().isBadRequest());

        verify(service).refreshToken("bad-token");
    }

    @Test
    void refreshToken__UserDoesNotHavePermission__ReturnsForbidden() throws Exception {
        when(service.refreshToken(anyString())).thenThrow(new UserDoesNotHavePermissionForEndpoint("No permission"));

        mvc.perform(put(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"refreshToken": "no-permission-token"}
                            """))
                .andExpect(status().isForbidden());

        verify(service).refreshToken("no-permission-token");
    }

    @Test
    void refreshToken__EndpointNotFound__ReturnsBadRequest() throws Exception {
        when(service.refreshToken(anyString())).thenThrow(new EndpointNotFoundException("Not found"));

        mvc.perform(put(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"refreshToken": "missing-endpoint"}
                            """))
                .andExpect(status().isBadRequest());

        verify(service).refreshToken("missing-endpoint");
    }

    @Test
    void refreshToken__TokenVerificationFailed__ReturnsUnauthorized() throws Exception {
        when(service.refreshToken(anyString())).thenThrow(new TokenDidNotPassVerificationException("Verification failed"));

        mvc.perform(put(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"refreshToken": "invalid-signature"}
                            """))
                .andExpect(status().isUnauthorized());

        verify(service).refreshToken("invalid-signature");
    }

    @Test
    void refreshToken__MethodArgumentNotValid__ReturnsBadRequest() throws Exception {
        mvc.perform(put(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {}
                            """))
                .andExpect(status().isBadRequest());
    }
}
