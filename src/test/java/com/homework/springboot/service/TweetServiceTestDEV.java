package com.homework.springboot.service;

import com.homework.springboot.exceptions.TweetDoesNotExist;
import com.homework.springboot.exceptions.UserAlreadyExists;
import com.homework.springboot.model.Tweet;
import com.homework.springboot.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class TweetServiceTestDEV {

    @Mock
    private TweetService tweetService;

    @Mock
    private UserService userService;

    @Test
    public void testSave() throws TweetDoesNotExist, UserAlreadyExists {
//        User user = new User(1L, "mare", "marePass", "mare@mare.com", new ArrayList<>());
//        userService.save(user);
//
//        Tweet tweetToBeSaved = new Tweet(1L, "Content1", new Date(), user);
//        tweetService.save(tweetToBeSaved);
//
//        when(tweetService.save(any(Tweet.class))).thenReturn(new Tweet());
//
//        Tweet foundTweet = tweetService.getById(1L);
//
//        assertEquals(tweetToBeSaved.getId(), foundTweet.getId());
//        assertEquals(tweetToBeSaved.getContent(), foundTweet.getContent());
//        assertEquals(tweetToBeSaved.getCreationDate(), foundTweet.getCreationDate());
//        assertEquals(tweetToBeSaved.getUserId(), foundTweet.getUserId());
    }

}
