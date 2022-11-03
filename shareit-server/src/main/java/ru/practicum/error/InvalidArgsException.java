package ru.practicum.error;

public class InvalidArgsException extends RuntimeException {
    public InvalidArgsException(String message) {
        super(message);
    }
}
