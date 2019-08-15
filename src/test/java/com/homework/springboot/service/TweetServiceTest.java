package com.homework.springboot.service;

import com.homework.springboot.exceptions.TweetDoesNotExist;
import com.homework.springboot.exceptions.UserAlreadyExists;
import com.homework.springboot.model.Tweet;
import com.homework.springboot.model.User;
import com.homework.springboot.repository.TweetRepository;
import com.homework.springboot.service.impl.TweetServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class TweetServiceTest {

    @Mock
    private TweetRepository tweetRepository;

    @InjectMocks
    private TweetServiceImpl tweetService;

    @Test
    public void testSave() throws TweetDoesNotExist, UserAlreadyExists {
        User user = new User(1L, "mare", "marePass", "mare@mare.com", new ArrayList<>());

        Tweet tweetToBeSaved = new Tweet(1L, "Content1", LocalDate.now(), user);

        when(tweetRepository.save(any(Tweet.class))).thenReturn(tweetToBeSaved);

        Tweet savedTweet = tweetService.save(tweetToBeSaved);

        assertThat(savedTweet.getContent()).isSameAs(tweetToBeSaved.getContent());
    }



}
