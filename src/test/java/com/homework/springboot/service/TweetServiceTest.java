package com.homework.springboot.service;

import com.homework.springboot.exceptions.TweetDoesNotExist;
import com.homework.springboot.exceptions.UserAlreadyExists;
import com.homework.springboot.model.Tweet;
import com.homework.springboot.model.User;
import com.homework.springboot.repository.TweetRepository;
import com.homework.springboot.service.impl.TweetServiceImpl;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class TweetServiceTest {

    @Mock
    private TweetRepository tweetRepository;

    @InjectMocks
    private TweetServiceImpl tweetService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testSave() throws TweetDoesNotExist, UserAlreadyExists {
        User user = new User(1L, "mare", "marePass", "mare@mare.com", new ArrayList<>());

        Tweet tweetToBeSaved = new Tweet(1L, "Content1", new Date(), user);

        when(tweetRepository.save(any(Tweet.class))).thenReturn(tweetToBeSaved);

        Tweet savedTweet = tweetService.save(tweetToBeSaved);

        assertThat(savedTweet.getContent()).isSameAs(tweetToBeSaved.getContent());
    }

    @Test
    public void testGetById() throws TweetDoesNotExist {
        User user = new User(1L, "mare", "marePass", "mare@mare.com", new ArrayList<>());

        Tweet tweetToBeSaved = new Tweet(1L, "Content1", new Date(), user);

        when(tweetRepository.findById(anyLong())).thenReturn(Optional.of(tweetToBeSaved));

        assertThat(tweetService.getById(tweetToBeSaved.getId())).isEqualTo(tweetToBeSaved);
    }

    @Test
    public void testUpdateContent() throws TweetDoesNotExist {
        User user = new User(1L, "mare", "marePass", "mare@mare.com", new ArrayList<>());

        Tweet tweetToBeUpdated = new Tweet(1L, "Content1", new Date(), user);

        when(tweetRepository.findById(anyLong())).thenReturn(Optional.of(tweetToBeUpdated));

        Tweet updateTweet = new Tweet(1L, "Content2", new Date(), user);

        Tweet actualTweet = tweetService.updateContent(updateTweet);

        assertThat(actualTweet.getContent()).isSameAs(updateTweet.getContent());
    }

    @Test
    public void testGetByIdShouldThrowTweetDoesNotExist() throws TweetDoesNotExist {
        expectedException.expect(TweetDoesNotExist.class);

        tweetService.getById(1L);

    }


}
