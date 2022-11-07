package ru.practicum.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.client.BaseClient;


@Service
public class UserClient extends BaseClient {

    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .build()
        );
    }

    public Object getAll() {
        return get("", null);
    }

    public Object getById(Integer userId) {
        return get("/" + userId, null);
    }

    public Object add(UserDto user) {
        return post("", null, user);
    }

    public Object update(UserDto user) {
        return put("", null, user);
    }

    public Object deleteById(Integer userId) {
        return delete("/" + userId, null);
    }

    public Object patch(Integer userId, UserDto user) {
        return patch("/" + userId, null, user);
    }
}
