package main.controller;

public class EntityNotFoundException extends Exception {
    public EntityNotFoundException() {
        super("Задача не найдена");
    }

    public EntityNotFoundException(String message) {
        super(message);
    }
}
