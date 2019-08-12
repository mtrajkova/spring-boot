package com.homework.springboot.service.impl;

import com.homework.springboot.exceptions.UserAlreadyExists;
import com.homework.springboot.exceptions.UserDoesNotExist;
import com.homework.springboot.model.Tweet;
import com.homework.springboot.model.User;
import com.homework.springboot.repository.TweetRepository;
import com.homework.springboot.repository.UserRepository;
import com.homework.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TweetRepository tweetRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, TweetRepository tweetRepository) {
        this.userRepository = userRepository;
        this.tweetRepository = tweetRepository;
    }

    @Override
    public List<User> getAllUsersThatHaveTweetedLastMonth() {
        List<User> usersThatTweetedLastMonth;

        usersThatTweetedLastMonth = userRepository.findAll().stream()
                .filter(user -> user.getLastMonthsTweets().size() > 0)
                .collect(Collectors.toList());

        return usersThatTweetedLastMonth;
    }

    @Override
    public User save(User user) throws UserAlreadyExists {
        if (userAlreadyExists(user)) {
            throw new UserAlreadyExists(user);
        }

        userRepository.save(user);
        return user;
    }

    @Override
    public void updatePassword(User user) throws UserDoesNotExist {
        if (!userAlreadyExists(user)) {
            throw new UserDoesNotExist();
        }

        User userToUpdate = userRepository.findById(user.getId()).get();
        userToUpdate.setPassword(user.getPassword());
        userRepository.save(userToUpdate);
    }

    @Override
    public void delete(User user) throws UserDoesNotExist {
        if (!userAlreadyExists(user)) {
            throw new UserDoesNotExist();
        }

        userRepository.delete(user);
    }

    @Override
    public List<Tweet> getTweetsForUser(Long id) throws UserDoesNotExist {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            throw new UserDoesNotExist();
        }

        return user.get().getTweets();
    }

    @Override
    public void deleteTweets(User user) throws UserDoesNotExist {
        if (!userAlreadyExists(user)) {
            throw new UserDoesNotExist();
        }

        User userToBeUpdated = userRepository.findById(user.getId()).get();
        userToBeUpdated.setTweets(new ArrayList<>());
        userRepository.save(userToBeUpdated);
    }

    @Override
    public List<Tweet> getTweetsOnAParticularDate(Long id, Date date) throws UserDoesNotExist {
        Optional<User> user = userRepository.findById(id);

        if (!user.isPresent()) {
            throw new UserDoesNotExist();
        }

        return tweetRepository.findByCreationDate(date);
    }

    private boolean userAlreadyExists(User user) {
        return userRepository.findByUsername(user.getUsername()).isPresent() || userRepository.findByEmail(user.getEmail()).isPresent();
    }
}
