package ru.practicum.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;

import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    BookingService bookingService;

    ReturnedBookingDto returnedBookingDto = new ReturnedBookingDto(
            1, null, null, null, null, Status.WAITING.toString());

    Collection<ReturnedBookingDto> returnedBookingDtos = List.of(
            new ReturnedBookingDto(
                    1, null, null, null, null, Status.WAITING.toString()),
            new ReturnedBookingDto(
                    2, null, null, null, null, Status.WAITING.toString()
            ));

    @Test
    void add() throws Exception {
        when(bookingService.add(anyInt(), any()))
                .thenReturn(returnedBookingDto);
        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(returnedBookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(returnedBookingDto.getId()), Integer.class))
                .andExpect(jsonPath("$.status", is(returnedBookingDto.getStatus())));
    }

    @Test
    void patchBooking() throws Exception {
        when(bookingService.patch(anyInt(), anyInt(), anyBoolean()))
                .thenReturn(returnedBookingDto);
        mvc.perform(patch("/bookings/1?approved=true")
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(returnedBookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(returnedBookingDto.getId()), Integer.class))
                .andExpect(jsonPath("$.status", is(returnedBookingDto.getStatus())));
    }

    @Test
    void getBooking() throws Exception {
        when(bookingService.getById(anyInt(), anyInt()))
                .thenReturn(returnedBookingDto);
        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(returnedBookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(returnedBookingDto.getId()), Integer.class))
                .andExpect(jsonPath("$.status", is(returnedBookingDto.getStatus())));
    }

    @Test
    void getAllBookingsByOwnerId() throws Exception {
        when(bookingService.getAllBookingsByOwnerId(anyInt(), anyString(), anyInt(), anyInt()))
                .thenReturn(returnedBookingDtos);
        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(returnedBookingDtos))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].status", containsInAnyOrder(
                        Status.WAITING.toString(), Status.WAITING.toString())));
    }

    @Test
    void getAllBookingsForAllItemsById() throws Exception {
        when(bookingService.getAllBookingsForAllItemsByOwnerId(anyInt(), anyString(), anyInt(), anyInt()))
                .thenReturn(returnedBookingDtos);
        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "1")
                        .content(mapper.writeValueAsString(returnedBookingDtos))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].status", containsInAnyOrder(
                        Status.WAITING.toString(), Status.WAITING.toString())));
    }
}