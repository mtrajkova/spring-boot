package com.homework.springboot.exceptions;

import com.homework.springboot.model.User;

public class UserDoesNotExist extends Exception {
    private String message;

    public UserDoesNotExist(User user) {
        this.message = "User " + user.toString() + " does not exist!";
    }

    @Override
    public String getMessage() {
        return message;
    }
}
