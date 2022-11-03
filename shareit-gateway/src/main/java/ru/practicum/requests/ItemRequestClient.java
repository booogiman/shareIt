package ru.practicum.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.BaseClient;

import java.util.Map;

@Service
public class ItemRequestClient extends BaseClient {

    private static final String API_PREFIX = "/requests";

    @Autowired
    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .build()
        );
    }

    public Object add(Integer userId, ItemRequestDto itemRequestDto) {
        return post("", userId, itemRequestDto);
    }

    public Object getAllUsersRequests(Integer userId) {
        return get("", userId);
    }

    public Object getAllRequests(Integer userId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("/all" + "?from=" + from + "&size=" + size, userId, parameters);
    }

    public Object getById(Integer userId, Integer requestId) {
        return get("/" + requestId, userId);
    }

}
