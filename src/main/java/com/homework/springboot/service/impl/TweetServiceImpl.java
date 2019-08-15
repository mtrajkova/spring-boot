package com.homework.springboot.service.impl;

import com.homework.springboot.exceptions.TweetDoesNotExist;
import com.homework.springboot.exceptions.UserDoesNotExist;
import com.homework.springboot.model.Tweet;
import com.homework.springboot.model.User;
import com.homework.springboot.repository.TweetRepository;
import com.homework.springboot.service.TweetService;
import com.homework.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TweetServiceImpl implements TweetService {

    private final TweetRepository tweetRepository;

    @Autowired
    public TweetServiceImpl(TweetRepository tweetRepository) {
        this.tweetRepository = tweetRepository;
    }

    @Override
    public Tweet getById(Long id) throws TweetDoesNotExist {
        if (!tweetRepository.findById(id).isPresent()) {
            throw new TweetDoesNotExist();
        }

        return tweetRepository.findById(id).get();
    }

    @Override
    public Tweet save(Tweet tweet) {
        tweetRepository.save(tweet);
        return tweet;
    }

    @Override
    public Tweet updateContent(Tweet tweet) throws TweetDoesNotExist {
        if (!tweetAlreadyExists(tweet)) {
            throw new TweetDoesNotExist();
        }

        Tweet tweetToBeUpdated = tweetRepository.findById(tweet.getId()).get();
        tweetToBeUpdated.setContent(tweet.getContent());
        tweetRepository.save(tweetToBeUpdated);

        return tweetToBeUpdated;
    }

    private boolean tweetAlreadyExists(Tweet tweet) {
        if (tweet.getId() == null)
            return false;

        return tweetRepository.findById(tweet.getId()).isPresent();
    }
}
