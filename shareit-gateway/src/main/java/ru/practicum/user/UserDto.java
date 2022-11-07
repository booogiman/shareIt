package ru.practicum.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class UserDto {
    private Integer id;
    @NotEmpty
    private String name;
    @NotEmpty
    @Email
    private String email;

    public UserDto(Integer id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
