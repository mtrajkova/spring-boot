package com.homework.springboot.controller;

import com.homework.springboot.exceptions.UserAlreadyExists;
import com.homework.springboot.exceptions.UserDoesNotExist;
import com.homework.springboot.model.User;
import com.homework.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity saveUser(@Valid @RequestBody User user) {
        try {
            userService.save(user);
            return new ResponseEntity(HttpStatus.CREATED);
        } catch (UserAlreadyExists userAlreadyExists) {
            System.out.println(userAlreadyExists.getMessage());
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    }

    @PutMapping
    public ResponseEntity updateUserPassword(@Valid @RequestBody User user) {
        try {
            userService.updatePassword(user);
            return new ResponseEntity(HttpStatus.OK);
        } catch (UserDoesNotExist userDoesNotExist) {
            System.out.println(userDoesNotExist.getMessage());
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
}
