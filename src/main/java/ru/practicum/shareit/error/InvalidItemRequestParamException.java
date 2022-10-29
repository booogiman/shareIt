package ru.practicum.shareit.error;

public class InvalidItemRequestParamException extends RuntimeException {
    public InvalidItemRequestParamException(String message) {
        super(message);
    }
}
