package ru.practicum.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .build()
        );
    }

    public Object add(Integer userId, ResultingBookingDto booking) {
        return post("", userId, booking);
    }

    public Object getById(Integer userId, Integer bookingId) {
        return get("/" + bookingId, userId);
    }

    public Object getAllBookingsByOwnerId(Integer userId, String state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state,
                "from", from,
                "page", size
        );
        return get("?state=" + state + "&from=" + from + "&size=" + size, userId, parameters);
    }

    public Object getAllBookingsForAllItemsByOwnerId(Integer userId, String state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state,
                "from", from,
                "page", size
        );
        return get("/owner" + "?state=" + state + "&from=" + from + "&size=" + size, userId, parameters);
    }

    public Object patch(Integer userId, Integer bookingId, Boolean approved) {
        Map<String, Object> parameters = Map.of(
                "approved", approved
        );
        return patch("/" + bookingId + "?approved=" + approved, userId, parameters);
    }
}
