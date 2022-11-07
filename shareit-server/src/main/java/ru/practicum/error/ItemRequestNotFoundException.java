package ru.practicum.error;

public class ItemRequestNotFoundException extends RuntimeException {

    public ItemRequestNotFoundException(String message) {
        super(message);
    }
}
