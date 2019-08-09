package com.homework.springboot.service.impl;

import com.homework.springboot.exceptions.UserAlreadyExists;
import com.homework.springboot.exceptions.UserDoesNotExist;
import com.homework.springboot.model.Tweet;
import com.homework.springboot.model.User;
import com.homework.springboot.repository.UserRepository;
import com.homework.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsersThatHaveTweetedLastMonth() {
        List<User> usersThatTweetedLastMonth = new ArrayList<>();

        usersThatTweetedLastMonth = userRepository.findAll().stream()
                .filter(user -> user.getLastMonthsTweets().size() > 0)
                .collect(Collectors.toList());

        return usersThatTweetedLastMonth;
    }

    @Override
    public void save(User user) throws UserAlreadyExists {
        if (userAlreadyExists(user)) {
            throw new UserAlreadyExists();
        }

        userRepository.save(user);
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
    public List<Tweet> getTweetsForUser(User user) throws UserDoesNotExist {
        if (!userAlreadyExists(user)) {
            throw new UserDoesNotExist();
        }

        return userRepository.findById(user.getId()).get().getTweets();
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
    public List<Tweet> getTweetsOnAParticularDate(User user, Date date) throws UserDoesNotExist {
        if (!userAlreadyExists(user)) {
            throw new UserDoesNotExist();
        }

        List<Tweet> tweetsOnDate;

        tweetsOnDate = userRepository.findById(user.getId()).get().getTweets().stream()
                .filter(tweet -> tweet.isOn(date))
                .collect(Collectors.toList());

        return tweetsOnDate;
    }

    private boolean userAlreadyExists(User user) {
        return userRepository.findById(user.getId()).isPresent();
    }
}
