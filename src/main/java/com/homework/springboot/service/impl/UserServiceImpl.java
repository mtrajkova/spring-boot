package com.homework.springboot.service.impl;

import com.homework.springboot.exceptions.UserAlreadyExists;
import com.homework.springboot.exceptions.UserDoesNotExist;
import com.homework.springboot.model.Tweet;
import com.homework.springboot.model.User;
import com.homework.springboot.model.dto.PasswordsDto;
import com.homework.springboot.repository.TweetRepository;
import com.homework.springboot.repository.UserRepository;
import com.homework.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TweetRepository tweetRepository;

    @Value("${user.password}")
    private String defaultPassword;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, TweetRepository tweetRepository) {
        this.userRepository = userRepository;
        this.tweetRepository = tweetRepository;
    }

    @Override
    public List<User> getAllUsersThatHaveTweetedLastMonth() {
        List<User> usersThatTweetedLastMonth =  new ArrayList<>();

        usersThatTweetedLastMonth = userRepository.findAll().stream()
                .filter(user -> !user.lastMonthsTweets().isEmpty())
                .collect(Collectors.toList());

        return usersThatTweetedLastMonth;
    }

    @Override
    public User save(User user) throws UserAlreadyExists {
        if (userAlreadyExists(user)) {
            throw new UserAlreadyExists(user);
        }

        if (user.getPassword().isEmpty()) {
            user.setPassword(defaultPassword);
        }

        userRepository.save(user);
        return user;
    }

    @Override
    public User getById(Long id) throws UserDoesNotExist {
        Optional<User> user = userRepository.findById(id);

        if (!user.isPresent()) {
            throw new UserDoesNotExist();
        }

        return user.get();
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User updatePassword(Long userId, PasswordsDto passwords) throws UserDoesNotExist {
        Optional<User> userToUpdate = userRepository.findById(userId);

        if (!userToUpdate.isPresent()) {
            throw new UserDoesNotExist();
        }

        if (userToUpdate.get().getPassword().equals(passwords.getOldPassword())) {
            userToUpdate.get().setPassword(passwords.getNewPassword());
            userRepository.save(userToUpdate.get());
        }

        return userToUpdate.get();
    }

    @Override
    public void delete(Long id) throws UserDoesNotExist {
        Optional<User> userToDelete = userRepository.findById(id);

        if (!userToDelete.isPresent()) {
            throw new UserDoesNotExist();
        }

        userRepository.delete(userToDelete.get());
    }

    @Override
    public void deleteTweetsForUser(Long id) throws UserDoesNotExist {
        Optional<User> user = userRepository.findById(id);

        if (!user.isPresent()) {
            throw new UserDoesNotExist();
        }

        for (Tweet tweet : tweetRepository.findByUserId(id)) {
            tweetRepository.delete(tweet);
        }
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
    public List<Tweet> getTweetsOnAParticularDate(Long id, LocalDate date) throws UserDoesNotExist {
        Optional<User> user = userRepository.findById(id);

        if (!user.isPresent()) {
            throw new UserDoesNotExist();
        }

        Date date1 = java.util.Date.from(date.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());

        return tweetRepository.findByCreationDateAndUserId(date1, id);
    }

    @Override
    public void addTweet(Tweet tweet, Long id) {
        User user = userRepository.findById(id).get();

        user.getTweets().add(tweet);
        userRepository.save(user);
    }

    private boolean userAlreadyExists(User user) {
        return userRepository.findByUsername(user.getUsername()).isPresent() || userRepository.findByEmail(user.getEmail()).isPresent();
    }

}
