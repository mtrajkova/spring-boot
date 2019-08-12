package com.homework.springboot.controller;

import com.homework.springboot.exceptions.TweetDoesNotExist;
import com.homework.springboot.model.Tweet;
import com.homework.springboot.service.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/tweets")
public class TweetController {

    private final TweetService tweetService;

    @Autowired
    public TweetController(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @PostMapping
    public ResponseEntity saveTweet(@RequestBody Tweet tweet) {
        tweetService.save(tweet);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Tweet> getTweetById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(tweetService.getById(id), HttpStatus.OK);
        } catch (TweetDoesNotExist tweetDoesNotExist) {
            System.out.println(tweetDoesNotExist.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping
    public ResponseEntity updateContent(@RequestBody Tweet tweet) {
        try {
            tweetService.updateContent(tweet);
            return new ResponseEntity(HttpStatus.OK);
        } catch (TweetDoesNotExist tweetDoesNotExist) {
            tweetDoesNotExist.printStackTrace();
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
}
