package ru.practicum.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;

import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    RequestService requestService;

    ItemRequestDto itemRequestDto = new ItemRequestDto(1, "Хотел бы поспользоваться щеткой для обуви",
            2, null, null);

    Collection<ItemRequestDto> itemRequestDtos = List.of(
            new ItemRequestDto(1, "Хотел бы поспользоваться щеткой для обуви", 2, LocalDateTime.now(), null),
            new ItemRequestDto(2, "Хотел бы поспользоваться паяльником", 3, LocalDateTime.now(), null));

    @Test
    void add() throws Exception {
        when(requestService.add(anyInt(), any()))
                .thenReturn(itemRequestDto);
        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Integer.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$.requester", is(itemRequestDto.getRequester())));
    }

    @Test
    void getAllUsersRequests() throws Exception {
        when(requestService.getAllUsersRequests(anyInt()))
                .thenReturn(itemRequestDtos);
        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(itemRequestDtos))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].description", containsInAnyOrder(
                        "Хотел бы поспользоваться щеткой для обуви", "Хотел бы поспользоваться паяльником")));
    }

    @Test
    void getAllRequests() throws Exception {
        when(requestService.getAllRequests(anyInt(), anyInt(), anyInt()))
                .thenReturn(itemRequestDtos);
        mvc.perform(get("/requests/all?from=0&size=5")
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(itemRequestDtos))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].description", containsInAnyOrder(
                        "Хотел бы поспользоваться щеткой для обуви", "Хотел бы поспользоваться паяльником")));
    }

    @Test
    void getRequestById() throws Exception {
        when(requestService.getById(anyInt(), anyInt()))
                .thenReturn(itemRequestDto);
        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Integer.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())))
                .andExpect(jsonPath("$.requester", is(itemRequestDto.getRequester())));
    }
}