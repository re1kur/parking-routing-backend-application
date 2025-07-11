package re1kur.is.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import re1kur.core.dto.Credentials;
import re1kur.core.exception.*;
import re1kur.core.payload.GenerateCodeRequest;
import re1kur.core.payload.LoginRequest;
import re1kur.core.payload.UserPayload;
import re1kur.is.service.AuthService;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = AuthenticationController.class)
class AuthenticationControllerTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockitoBean
    AuthService service;

    private static final String URL = "/api/auth";

    private UUID id;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
    }

    @Test
    void register__InvalidPayload__ReturnsBadRequest() throws Exception {
        UserPayload payload = UserPayload.builder()
                .phoneNumber("")
                .email("")
                .firstName("")
                .lastName("")
                .build();

        mvc.perform(post(URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(service);
    }

    @Test
    void register__ReturnsCreated() throws Exception {
        UserPayload payload = UserPayload.builder()
                .phoneNumber("9999999999")
                .email("mail@mail.com")
                .firstName("firstname")
                .lastName("lastname")
                .build();

        when(service.register(UserPayload.builder()
                .phoneNumber("9999999999")
                .email("mail@mail.com")
                .firstName("firstname")
                .lastName("lastname")
                .build())).thenReturn(id);

        mvc.perform(post(URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(id)));

        verify(service, times(1)).register(payload);
    }

    @Test
    void register__EmailIsOccupied__ReturnsConflict() throws Exception {
        UserPayload payload = UserPayload.builder()
                .phoneNumber("9999999999")
                .email("mail@mail.com")
                .firstName("firstname")
                .lastName("lastname")
                .build();

        when(service.register(UserPayload.builder()
                .phoneNumber("9999999999")
                .email("mail@mail.com")
                .firstName("firstname")
                .lastName("lastname")
                .build())).thenThrow(EmailAlreadyRegisteredException.class);

        mvc.perform(post(URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isConflict());

        verify(service, times(1)).register(payload);
    }

    @Test
    void register__PhoneIsOccupied__ReturnsConflict() throws Exception {
        UserPayload payload = UserPayload.builder()
                .phoneNumber("9999999999")
                .email("mail@mail.com")
                .firstName("firstname")
                .lastName("lastname")
                .build();

        when(service.register(UserPayload.builder()
                .phoneNumber("9999999999")
                .email("mail@mail.com")
                .firstName("firstname")
                .lastName("lastname")
                .build())).thenThrow(PhoneNumberAlreadyRegisteredException.class);

        mvc.perform(post(URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isConflict());

        verify(service, times(1)).register(payload);
    }

    @Test
    void login__ReturnsOk() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .phoneNumber("9999999999")
                .code("123123")
                .build();

        Credentials cred = Credentials.builder()
                .phone("9999999999")
                .email("mail@mail.com")
                .sub(id.toString())
                .build();

        when(service.login(LoginRequest.builder()
                .phoneNumber("9999999999")
                .code("123123")
                .build())).thenReturn(Credentials.builder()
                .phone("9999999999")
                .email("mail@mail.com")
                .sub(id.toString())
                .build());

        mvc.perform(post(URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(cred)));

        verify(service, times(1)).login(request);
    }

    @Test
    void login__UserNotFound__ReturnsBadRequest() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .phoneNumber("9999999999")
                .code("123123")
                .build();

        when(service.login(LoginRequest.builder()
                .phoneNumber("9999999999")
                .code("123123")
                .build())).thenThrow(UserNotFoundException.class);

        mvc.perform(post(URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).login(request);
    }

    @Test
    void login__CodeMismatches__ReturnsBadRequest() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .phoneNumber("9999999999")
                .code("123123")
                .build();

        when(service.login(LoginRequest.builder()
                .phoneNumber("9999999999")
                .code("123123")
                .build())).thenThrow(CodeMismatchException.class);

        mvc.perform(post(URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());

        verify(service, times(1)).login(request);
    }

    @Test
    void login__CodeNotFound__ReturnsBadRequest() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .phoneNumber("9999999999")
                .code("123123")
                .build();

        when(service.login(LoginRequest.builder()
                .phoneNumber("9999999999")
                .code("123123")
                .build())).thenThrow(CodeNotFoundException.class);

        mvc.perform(post(URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).login(request);
    }

    @Test
    void login__CodeHasExpired__ReturnsGone() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .phoneNumber("9999999999")
                .code("123123")
                .build();

        when(service.login(LoginRequest.builder()
                .phoneNumber("9999999999")
                .code("123123")
                .build())).thenThrow(CodeHasExpiredException.class);

        mvc.perform(post(URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isGone());

        verify(service, times(1)).login(request);
    }

    @Test
    void login__InvalidPayload__ReturnsBadRequest() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .phoneNumber("")
                .code("")
                .build();

        mvc.perform(post(URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(service);
    }

    @Test
    void generateCode__ReturnsCreated() throws Exception {
        GenerateCodeRequest request = new GenerateCodeRequest("9999999999");

        doNothing().when(service).generateCode(new GenerateCodeRequest("9999999999"));

        mvc.perform(post(URL + "/code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(service, times(1)).generateCode(request);
    }

    @Test
    void generateCode__InvalidPayload__ReturnsBadRequest() throws Exception {
        GenerateCodeRequest request = new GenerateCodeRequest("");

        doNothing().when(service).generateCode(new GenerateCodeRequest(""));

        mvc.perform(post(URL + "/code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(service);
    }
}