package com.homework.springboot.service.impl;

import com.homework.springboot.exceptions.TweetDoesNotExist;
import com.homework.springboot.model.Tweet;
import com.homework.springboot.repository.TweetRepository;
import com.homework.springboot.service.TweetService;
import com.homework.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TweetServiceImpl implements TweetService {

    private final TweetRepository tweetRepository;
    private final UserService userService;

    @Autowired
    public TweetServiceImpl(TweetRepository tweetRepository, UserService userService) {
        this.tweetRepository = tweetRepository;
        this.userService = userService;
    }

    @Override
    public Tweet getById(Long id) throws TweetDoesNotExist {
        if (!tweetRepository.findById(id).isPresent()) {
            throw new TweetDoesNotExist();
        }

        return tweetRepository.findById(id).get();
    }

    @Override
    public void save(Tweet tweet) {
        tweetRepository.save(tweet);
    }

    @Override
    public void updateContent(Tweet tweet) throws TweetDoesNotExist {
        if (!tweetAlreadyExists(tweet)) {
            throw new TweetDoesNotExist();
        }

        Tweet tweetToBeUpdated = tweetRepository.findById(tweet.getId()).get();
        tweetToBeUpdated.setContent(tweet.getContent());
        tweetRepository.save(tweetToBeUpdated);
    }

    private boolean tweetAlreadyExists(Tweet tweet) {
        if (tweet.getId() == null)
            return false;

        return tweetRepository.findById(tweet.getId()).isPresent();
    }
}
