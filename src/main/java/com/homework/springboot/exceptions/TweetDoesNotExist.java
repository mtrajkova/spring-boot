package com.homework.springboot.exceptions;

public class TweetDoesNotExist extends Exception {
    private String message;

    public TweetDoesNotExist() {
        this.message = "Tweet does not exist.";
    }

    @Override
    public String getMessage() {
        return message;
    }
}
