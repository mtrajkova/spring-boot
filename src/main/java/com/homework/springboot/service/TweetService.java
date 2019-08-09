package com.homework.springboot.service;

import com.homework.springboot.exceptions.TweetDoesNotExist;
import com.homework.springboot.model.Tweet;

public interface TweetService {
    Tweet getById(Long id) throws TweetDoesNotExist;
    void save(Tweet tweet);
    void updateContent(Tweet tweet) throws TweetDoesNotExist;
}
