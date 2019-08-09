package com.homework.springboot.service;

import com.homework.springboot.exceptions.TweetDoesNotExist;
import com.homework.springboot.model.Tweet;

public interface TweetService {
    Tweet getById(Long id);
    void save(Tweet tweet);
    void updateContent(Tweet tweet) throws TweetDoesNotExist;
}
