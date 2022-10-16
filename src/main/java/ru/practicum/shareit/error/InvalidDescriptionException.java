package ru.practicum.shareit.error;

public class InvalidDescriptionException extends RuntimeException {

    public InvalidDescriptionException(String message) {
        super(message);
    }
}
