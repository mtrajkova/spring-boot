package com.homework.springboot.service;

import com.homework.springboot.exceptions.UserAlreadyExists;
import com.homework.springboot.exceptions.UserDoesNotExist;
import com.homework.springboot.model.Tweet;
import com.homework.springboot.model.User;
import com.homework.springboot.model.dto.PasswordsDto;

import java.time.LocalDate;
import java.util.List;

public interface UserService {
    User save(User user) throws UserAlreadyExists;

    User getById(Long id) throws UserDoesNotExist;

    List<User> getAll();

    User updatePassword(Long userId, PasswordsDto passwords) throws UserDoesNotExist;

    void delete(Long id) throws UserDoesNotExist;

    void deleteTweetsForUser(Long id) throws UserDoesNotExist;

    List<User> getAllUsersThatHaveTweetedLastMonth();

    List<Tweet> getTweetsForUser(Long userId) throws UserDoesNotExist;

    List<Tweet> getTweetsOnAParticularDate(Long id, LocalDate date) throws UserDoesNotExist;

    void addTweet(Tweet tweet, Long id);
}

