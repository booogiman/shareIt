package ru.practicum.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.BaseClient;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .build()
        );
    }

    public Object add(Integer userId, ItemDto item) {
        return post("", userId, item);
    }

    public Object getById(Integer userId, Integer itemId) {
        return get("/" + itemId, userId);
    }

    public Object getAll(Integer userId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "page", size
        );
        return get("?from=" + from + "&size=" + size, userId, parameters);
    }

    public Object search(String text, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "text", text,
                "from", from,
                "page", size
        );
        return get("/search" + "?text=" + text + "&from=" + from + "&size=" + size, null, parameters);
    }

    public Object addComment(Integer userId, Integer itemId, CommentDto comment) {
        return post("/" + itemId + "/comment", userId, comment);
    }

    public Object patch(Integer userId, Integer itemId, ItemDto item) {
        return patch("/" + itemId, userId, item);
    }

}
