package ru.practicum.error;

public class InvalidAvailableException extends RuntimeException {

    public InvalidAvailableException(String message) {
        super(message);
    }
}
