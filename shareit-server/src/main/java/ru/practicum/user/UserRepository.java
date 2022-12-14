package ru.practicum.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    Boolean existsUserById(Integer id);
    Boolean existsBy();

}
