package ru.practicum.error;

public class InvalidItemRequestException extends RuntimeException {
    public InvalidItemRequestException(String message) {
        super(message);
    }
}
