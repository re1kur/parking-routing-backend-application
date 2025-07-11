package re1kur.pars.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import re1kur.core.dto.MakeDto;
import re1kur.core.exception.MakeAlreadyExistsException;
import re1kur.core.exception.MakeNotFoundException;
import re1kur.core.payload.MakePayload;
import re1kur.pars.service.MakeService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MakeController.class)
class MakeControllerTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockitoBean
    MakeService service;
    private static final String URL = "/api/makes";

    @Test
    void create__ReturnsCreated() throws Exception {
        MakePayload payload = MakePayload.builder()
                .name("make")
                .build();
        MakeDto expected = MakeDto.builder()
                .name("make")
                .build();

        when(service.create(MakePayload.builder()
                .name("make")
                .build())).thenReturn(MakeDto.builder()
                .name("make")
                .build());

        mvc.perform(post(URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(expected)));

        verify(service, times(1)).create(payload);
    }

    @Test
    void create__InvalidPayload__ReturnsBadRequest() throws Exception {
        MakePayload payload = MakePayload.builder()
                .name("")
                .build();

        mvc.perform(post(URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(service);
    }

    @Test
    void create__MakeNameIsOccupied__ReturnsConflict() throws Exception {
        MakePayload payload = MakePayload.builder()
                .name("make")
                .build();

        when(service.create(MakePayload.builder()
                .name("make")
                .build())).thenThrow(MakeAlreadyExistsException.class);

        mvc.perform(post(URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isConflict());

        verify(service, times(1)).create(payload);
    }

    @Test
    void get__ReturnsMakeDto() throws Exception {
        Integer makeId = 1;
        MakeDto expected = MakeDto.builder()
                .name("name")
                .build();

        when(service.get(1)).thenReturn(MakeDto.builder()
                .name("name")
                .build());

        mvc.perform(get(URL + "/get")
                        .param("id", makeId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expected)));

        verify(service, times(1)).get(makeId);
    }

    @Test
    void get__MakeNotFound__ReturnsBadRequest() throws Exception {
        Integer makeId = 1;

        when(service.get(1)).thenThrow(MakeNotFoundException.class);

        mvc.perform(get(URL + "/get")
                        .param("id", makeId.toString()))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).get(makeId);
    }

    @Test
    void update__ReturnsOk() throws Exception {
        Integer makeId = 1;
        MakePayload payload = MakePayload.builder()
                .name("newName")
                .build();
        MakeDto expected = MakeDto.builder()
                .name("newName")
                .build();

        when(service.update(MakePayload.builder()
                .name("newName")
                .build(), 1)).thenReturn(MakeDto.builder()
                .name("newName")
                .build());

        mvc.perform(put(URL + "/%d/update".formatted(makeId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expected)));

        verify(service, times(1)).update(payload, makeId);
    }

    @Test
    void update__InvalidPayload__ReturnsBadRequest() throws Exception {
        Integer makeId = 1;
        MakePayload payload = MakePayload.builder()
                .name("")
                .build();

        mvc.perform(put(URL + "/%d/update".formatted(makeId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(service);
    }

    @Test
    void update__MakeNotFound__ReturnsBadRequest() throws Exception {
        Integer makeId = 1;
        MakePayload payload = MakePayload.builder()
                .name("newName")
                .build();

        when(service.update(MakePayload.builder()
                .name("newName")
                .build(), 1)).thenThrow(MakeNotFoundException.class);

        mvc.perform(put(URL + "/%d/update".formatted(makeId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).update(payload, makeId);
    }

    @Test
    void update__MakeAlreadyExists__ReturnsConflict() throws Exception {
        Integer makeId = 1;
        MakePayload payload = MakePayload.builder()
                .name("newName")
                .build();

        when(service.update(MakePayload.builder()
                .name("newName")
                .build(), 1)).thenThrow(MakeAlreadyExistsException.class);

        mvc.perform(put(URL + "/%d/update".formatted(makeId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(payload)))
                .andExpect(status().isConflict());

        verify(service, times(1)).update(payload, makeId);
    }

    @Test
    void delete__ReturnsNoContent() throws Exception {
        Integer makeId = 1;

        doNothing().when(service).delete(1);

        mvc.perform(delete(URL + "/%d/delete".formatted(makeId)))
                .andExpect(status().isNoContent());

        verify(service, times(1)).delete(makeId);
    }

    @Test
    void delete__MakeNotFound__ReturnsBadRequest() throws Exception {
        Integer makeId = 1;

        doThrow(MakeNotFoundException.class).when(service).delete(1);

        mvc.perform(delete(URL + "/%d/delete".formatted(makeId)))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).delete(makeId);
    }

}