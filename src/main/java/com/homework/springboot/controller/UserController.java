package com.homework.springboot.controller;

import com.homework.springboot.exceptions.UserAlreadyExists;
import com.homework.springboot.exceptions.UserDoesNotExist;
import com.homework.springboot.model.Tweet;
import com.homework.springboot.model.User;
import com.homework.springboot.service.TweetService;
import com.homework.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;
    private final TweetService tweetService;

    @Autowired
    public UserController(UserService userService, TweetService tweetService) {
        this.userService = userService;
        this.tweetService = tweetService;
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

    @PostMapping("/tweets")
    public ResponseEntity addTweet(@RequestBody @Valid Tweet tweet) {
        tweetService.save(tweet);

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        try {
            User user = userService.getById(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (UserDoesNotExist userDoesNotExist) {
            userDoesNotExist.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping(value = "/tweeted-last-month")
    public List<User> getAllUsersThatHaveTweetedLastMonth() {
        return userService.getAllUsersThatHaveTweetedLastMonth();
    }

    @GetMapping(value = "/{id}/tweets")
    public ResponseEntity<List<Tweet>> getTweetsForUser(@PathVariable Long id) {
        try {
            List<Tweet> tweets = userService.getTweetsForUser(id);
            return new ResponseEntity<>(tweets, HttpStatus.OK);
        } catch (UserDoesNotExist userDoesNotExist) {
            System.out.println(userDoesNotExist.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/{id}/tweetsOnDate")
    public ResponseEntity<List<Tweet>> getTweetsOnAParticularDate(@PathVariable Long id, @RequestParam("date") @DateTimeFormat(pattern = "dd.MM.yyyy") Date date) {
        try {
            List<Tweet> tweets = userService.getTweetsOnAParticularDate(id, date);
            return new ResponseEntity<>(tweets, HttpStatus.OK);
        } catch (UserDoesNotExist userDoesNotExist) {
            System.out.println(userDoesNotExist.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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

    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id) {
        try {
            userService.delete(id);
            return new ResponseEntity(HttpStatus.OK);
        } catch (UserDoesNotExist userDoesNotExist) {
            System.out.println(userDoesNotExist.getMessage());
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/{id}/tweets")
    public ResponseEntity deleteTweetsForUser(@PathVariable Long id) {
        try {
            userService.deleteTweetsForUser(id);
            return new ResponseEntity(HttpStatus.OK);
        } catch (UserDoesNotExist userDoesNotExist) {
            System.out.println(userDoesNotExist.getMessage());
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

}
