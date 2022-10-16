package ru.practicum.shareit.error;

public class InvalidIdException extends RuntimeException {

    public InvalidIdException(String message) {
        super(message);
    }
}
