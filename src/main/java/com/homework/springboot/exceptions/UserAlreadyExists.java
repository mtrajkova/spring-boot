package com.homework.springboot.exceptions;

import com.homework.springboot.model.User;

public class UserAlreadyExists extends Exception {
    private String message;

    public UserAlreadyExists(User user) {
        this.message = "User " + user.toString() + " already exists!";
    }

    @Override
    public String getMessage() {
        return message;
    }
}
