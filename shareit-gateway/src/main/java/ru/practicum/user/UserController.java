package ru.practicum.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserClient userClient;

    @Autowired
    public UserController(UserClient userClient) {
        this.userClient = userClient;
    }


    @PostMapping
    public Object add(@RequestBody @Valid UserDto user) {
        return userClient.add(user);
    }

    @PutMapping
    public Object update(@RequestBody @Valid UserDto user) {
        return userClient.update(user);
    }

    @PatchMapping("/{id}")
    public Object patch(@PathVariable Integer id,
                         @RequestBody UserDto user) {
        return userClient.patch(id, user);
    }

    @GetMapping("/{id}")
    public Object get(@PathVariable Integer id) {
        return userClient.getById(id);
    }

    @GetMapping
    public Object getAll() {
        return userClient.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        userClient.deleteById(id);
    }
}
