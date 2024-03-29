package com.homework.springboot.controller;

import com.google.gson.Gson;
import com.homework.springboot.model.Tweet;
import com.homework.springboot.model.User;
import com.homework.springboot.repository.UserRepository;
import com.homework.springboot.service.impl.TweetServiceImpl;
import com.homework.springboot.service.impl.UserServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
public class TweetControllerIT {

    private static final String BASE_URL = "/tweets";
    private static final String URL_GET_TWEET_BY_ID = "/tweets/{id}";

    private MockMvc mockMvc;

    private Gson gson;

    @Autowired
    private TweetServiceImpl tweetService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private User user;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        gson = new Gson();
        user = new User()
                .withEmail("mare@mare.com")
                .withPassword("mare")
                .withUsername("mare");
    }

    @Test
    public void testSaveTweet() throws Exception {
        userService.save(user);

        //problem so LocalDate serijalizacija
        Tweet tweet = new Tweet()
                .withContent("mare is cool")
                .withUser(user);

        String tweetJsonString = gson.toJson(tweet);

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(tweetJsonString)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void testGetTweetById() throws Exception {
        userService.save(user);

        Tweet tweet = new Tweet()
                .withContent("mare is cool")
                .withUser(user);

        tweetService.save(tweet);

        mockMvc.perform(get(URL_GET_TWEET_BY_ID, tweet.getId()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void testUpdateContentResponseStatus() throws Exception {
        userService.save(user);

        Tweet tweet = new Tweet()
                .withContent("mare is cool")
                .withUser(user);

        tweetService.save(tweet);

        Tweet tweetToUpdate = new Tweet(tweet.getId(), "newContent", tweet.getCreationDate(), user);

        mockMvc.perform(put(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(gson.toJson(tweetToUpdate)))
                .andExpect(status().isOk());
    }

    @After
    public void cleanUp() {
        userRepository.deleteAll();
    }
}
