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
import re1kur.core.dto.UserDto;
import re1kur.core.dto.UserInformationDto;
import re1kur.core.payload.UserInformationPayload;
import re1kur.is.service.UserService;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserInformationController.class)
@ExtendWith(MockitoExtension.class)
class UserInformationControllerTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockitoBean
    UserService service;

    private static final String URL = "/api/users";

    private UUID id;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
    }

    @Test
    void getInfo__ValidToken__ReturnsUserInfo() throws Exception {
        String token = "Bearer token";
        UserInformationDto dto = UserInformationDto.builder().build();
        UserDto userDto = new UserDto(id.toString(), "email@mail.com", "9999999999", true, dto);

        when(service.getPersonalInfo(token)).thenReturn(userDto);

        mvc.perform(get(URL + "/info")
                        .header("Authorization", token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(userDto)));

        verify(service, times(1)).getPersonalInfo(token);
    }

    @Test
    void updateInfo__ValidPayload__ReturnsUpdatedUser() throws Exception {
        String token = "Bearer token";
        UserInformationPayload payload = new UserInformationPayload("first", "last", "middle");
        UserInformationDto dto = UserInformationDto.builder().build();
        UserDto userDto = new UserDto(id.toString(), "email@mail.com", "9999999999", true, dto);

        when(service.updateUserInfo(payload, token)).thenReturn(userDto);

        mvc.perform(put(URL + "/info/update")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(userDto)));

        verify(service, times(1)).updateUserInfo(payload, token);
    }

    @Test
    void identifyById__Exists__ReturnsOk() throws Exception {
        UUID id = UUID.randomUUID();
        when(service.existsById(id)).thenReturn(true);

        mvc.perform(get(URL + "/identify")
                        .param("id", id.toString()))
                .andExpect(status().isOk());

        verify(service, times(1)).existsById(id);
    }

    @Test
    void identifyById__NotExists__ReturnsBadRequest() throws Exception {
        UUID id = UUID.randomUUID();
        when(service.existsById(id)).thenReturn(false);

        mvc.perform(get(URL + "/identify")
                        .param("id", id.toString()))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).existsById(id);
    }

    @Test
    void updateInfo__InvalidPayload__ReturnsBadRequest() throws Exception {
        String token = "Bearer token";
        UserInformationPayload payload = new UserInformationPayload("", "", null);

        mvc.perform(put(URL + "/info/update")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(service);
    }

}