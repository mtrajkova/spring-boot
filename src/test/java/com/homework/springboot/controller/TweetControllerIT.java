package com.homework.springboot.controller;

import com.google.gson.Gson;
import com.homework.springboot.model.Tweet;
import com.homework.springboot.model.User;
import com.homework.springboot.service.TweetService;
import com.homework.springboot.service.impl.TweetServiceImpl;
import com.homework.springboot.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TweetControllerIT {

    private static final String URL_TWEET_SAVE = "/tweets";

    private MockMvc mockMvc;

    private Gson gson;

    @Autowired
    private TweetServiceImpl tweetService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        gson = new Gson();
    }

    @Test
    public void testSaveTweet() throws Exception {
             //problem so LocalDate serijalizacija
        Tweet tweet = new Tweet()
                .withContent("mare is cool")
                .withUser(new User());

        String tweetJsonString = gson.toJson(tweet);

        mockMvc.perform(post(URL_TWEET_SAVE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(tweetJsonString)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
}
