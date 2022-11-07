package ru.practicum.error;

public class InvalidItemRequestParamException extends RuntimeException {
    public InvalidItemRequestParamException(String message) {
        super(message);
    }
}
