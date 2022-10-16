package ru.practicum.shareit.error;

public class InvalidNameException extends RuntimeException {

    public InvalidNameException(String message) {
        super(message);
    }
}
