package com.homework.springboot.service;

import com.homework.springboot.exceptions.TweetDoesNotExist;
import com.homework.springboot.model.Tweet;

public interface TweetService {
    void save(Tweet tweet);
    Tweet getById(Long id) throws TweetDoesNotExist;
    void updateContent(Tweet tweet) throws TweetDoesNotExist;
}
