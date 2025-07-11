package re1kur.authz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import re1kur.authz.service.AuthzService;
import re1kur.core.dto.JwtToken;
import re1kur.core.exception.*;
import re1kur.core.payload.LoginRequest;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthzController.class)
class AuthzControllerTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockitoBean
    AuthzService service;
    private JwtToken expected;

    private static final String URL = "/api/authz";

    @BeforeEach
    void setUp() {
        expected = JwtToken.builder()
                .accessToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG" +
                        "9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.KMUFsIDTnFmyG3nMiGM6H9FNFUROf3wh7SmqJp-QV30")
                .refreshToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG" +
                        "9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.KMUFsIDTnFmyG3nMiGM6H9FNFUROf3wh7SmqJp-QV30")
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .refreshExpiresAt(LocalDateTime.now().plusMinutes(10))
                .build();
    }

    @Test
    void login__ReturnsOk() throws Exception{
        LoginRequest request = LoginRequest.builder()
                .code("123123")
                .phoneNumber("9999999999")
                .build();


        when(service.login(LoginRequest.builder()
                .code("123123")
                .phoneNumber("9999999999")
                .build())).thenReturn(expected);

        mvc.perform(post(URL + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expected)));

        verify(service, times(1)).login(request);
    }

    @Test
    void login__IdentityClientDidNotIdentified__ReturnsUnauthorized() throws Exception{
        LoginRequest request = LoginRequest.builder()
                .code("123123")
                .phoneNumber("9999999999")
                .build();


        when(service.login(LoginRequest.builder()
                .code("123123")
                .phoneNumber("9999999999")
                .build())).thenThrow(
                        new IdentityServiceAuthenticationException("body", HttpStatus.UNAUTHORIZED.value()));

        mvc.perform(post(URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());

        verify(service, times(1)).login(request);
    }

    @Test
    void login__InvalidPayload__ReturnsBadRequest() throws Exception{
        LoginRequest request = LoginRequest.builder()
                .code("")
                .phoneNumber("")
                .build();

        mvc.perform(post(URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(service);
    }

    @Test
    void authorizeRequest__ReturnsOk() throws Exception {
        String authHeader = "auth-header";
        String uriHeader = "uri-header";
        String methodHeader = "method-header";

        doNothing().when(service).authorizeRequest("auth-header", "uri-header", "method-header");

        mvc.perform(post(URL + "/authorize")
                        .header(HttpHeaders.AUTHORIZATION, authHeader)
                        .header("X-Original-URI", uriHeader)
                        .header("X-Original-Method", methodHeader)
                )
                .andExpect(status().isOk());

        verify(service, times(1)).authorizeRequest(authHeader, uriHeader, methodHeader);
    }

    @Test
    void authorizeRequest__InvalidToken__ReturnsBadRequest() throws Exception {
        String authHeader = "auth-header";
        String uriHeader = "uri-header";
        String methodHeader = "method-header";

        doThrow(InvalidTokenException.class).when(service).authorizeRequest("auth-header", "uri-header", "method-header");

        mvc.perform(post(URL + "/authorize")
                        .header(HttpHeaders.AUTHORIZATION, authHeader)
                        .header("X-Original-URI", uriHeader)
                        .header("X-Original-Method", methodHeader)
                )
                .andExpect(status().isBadRequest());

        verify(service, times(1)).authorizeRequest(authHeader, uriHeader, methodHeader);
    }

    @Test
    void authorizeRequest__TokenDidNotPassedVerification__ReturnsUnauthorized() throws Exception {
        String authHeader = "auth-header";
        String uriHeader = "uri-header";
        String methodHeader = "method-header";

        doThrow(TokenDidNotPassVerificationException.class).when(service).authorizeRequest("auth-header", "uri-header", "method-header");

        mvc.perform(post(URL + "/authorize")
                        .header(HttpHeaders.AUTHORIZATION, authHeader)
                        .header("X-Original-URI", uriHeader)
                        .header("X-Original-Method", methodHeader)
                )
                .andExpect(status().isUnauthorized());

        verify(service, times(1)).authorizeRequest(authHeader, uriHeader, methodHeader);
    }

    @Test
    void authorizeRequest__EndpointNotFound__ReturnsBadRequest() throws Exception {
        String authHeader = "auth-header";
        String uriHeader = "uri-header";
        String methodHeader = "method-header";

        doThrow(EndpointNotFoundException.class).when(service).authorizeRequest("auth-header", "uri-header", "method-header");

        mvc.perform(post(URL + "/authorize")
                        .header(HttpHeaders.AUTHORIZATION, authHeader)
                        .header("X-Original-URI", uriHeader)
                        .header("X-Original-Method", methodHeader)
                )
                .andExpect(status().isBadRequest());

        verify(service, times(1)).authorizeRequest(authHeader, uriHeader, methodHeader);
    }

    @Test
    void authorizeRequest__UserDoesNotHavePermission__ReturnsForbidden() throws Exception {
        String authHeader = "auth-header";
        String uriHeader = "uri-header";
        String methodHeader = "method-header";

        doThrow(UserDoesNotHavePermissionForEndpoint.class).when(service).authorizeRequest("auth-header", "uri-header", "method-header");

        mvc.perform(post(URL + "/authorize")
                        .header(HttpHeaders.AUTHORIZATION, authHeader)
                        .header("X-Original-URI", uriHeader)
                        .header("X-Original-Method", methodHeader)
                )
                .andExpect(status().isForbidden());

        verify(service, times(1)).authorizeRequest(authHeader, uriHeader, methodHeader);
    }

}