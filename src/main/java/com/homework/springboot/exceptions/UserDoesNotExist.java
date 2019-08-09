package com.homework.springboot.exceptions;

public class UserDoesNotExist extends Exception {
    private String message;

    public UserDoesNotExist() {
        this.message = "User does not exist!";
    }

    @Override
    public String getMessage() {
        return message;
    }
}
