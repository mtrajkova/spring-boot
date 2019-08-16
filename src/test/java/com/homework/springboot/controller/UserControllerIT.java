package com.homework.springboot.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.homework.springboot.model.Tweet;
import com.homework.springboot.model.User;
import com.homework.springboot.model.dto.PasswordsDto;
import com.homework.springboot.model.serialization.JsonDateSerializer;
import com.homework.springboot.repository.TweetRepository;
import com.homework.springboot.repository.UserRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
public class UserControllerIT {

    private static final String BASE_URL = "/users";
    private static final String URL_USER_BY_ID = "/users/{id}";
    private static final String URL_GET_USERS_THAT_TWEETED_LAST_MONTH = "/users/tweeted-last-month";
    private static final String URL_TWEETS_FOR_USER = "/users/{id}/tweets";
    private static final String URL_GET_TWEETS_FOR_USER_ON_DATE = "/users/{id}/tweets-on-date";

    private MockMvc mockMvc;

    private Gson gson;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(LocalDate.class, new JsonDateSerializer()).create();
    }

    @Test
    public void testSaveUserResponseStatus() throws Exception {
        User user = new User()
                .withEmail("mare@mare.com")
                .withPassword("mare")
                .withUsername("mare");

        String userJsonString = gson.toJson(user);

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(userJsonString)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

    }

    @Test
    public void testSaveUserResponse() throws Exception {
        User user = new User()
                .withEmail("mare@mare.com")
                .withPassword("mare")
                .withUsername("mare");

        String userJsonString = gson.toJson(user);

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(userJsonString)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<User> users = userRepository.findAll();
        User foundUser = users.get(0);

        assertThat(user.getUsername()).isEqualTo(foundUser.getUsername());
    }

    @Test
    public void testGetAllUsers() throws Exception {
        List<User> users = Stream.of(new User("m", "m", "M@m.com"), new User("a", "a", "a@a.com")).collect(Collectors.toList());

        for (User user : users) {
            userRepository.save(user);
        }

        mockMvc.perform(get(BASE_URL))
                .andExpect(content().json(gson.toJson(users)));
    }


    @Test
    public void testGetUser() throws Exception {
        User user = new User("a", "a", "a@a.com");
        userRepository.save(user);

        mockMvc.perform(get(URL_USER_BY_ID, user.getId()))
                .andExpect(content().json(gson.toJson(user)));
    }

    @Test
    public void testGetAllUsersThatTweetedLastMonth() throws Exception {
        User user1 = new User("m", "m", "M@m.com");
        User user2 = new User("a", "a", "a@a.com");
        User user3 = new User("b", "v", "b@a.com");
        List<User> users = Stream.of(user1, user2, user3).collect(Collectors.toList());

        for (User user : users) {
            userRepository.save(user);
        }

        tweetRepository.save(new Tweet("asdf", LocalDate.now().minusMonths(1), user1));
        tweetRepository.save(new Tweet("hfhfhfhfhf", LocalDate.now().minusMonths(1), user2));

        mockMvc.perform(get(URL_GET_USERS_THAT_TWEETED_LAST_MONTH))
                .andExpect(content().json(gson.toJson(users)));
    }

    //DATE SERIALIZATION
    @Test
    public void testGetTweetsForUser() throws Exception {
        User user1 = new User("m", "m", "M@m.com");
        userRepository.save(user1);

        Tweet tweet1 = new Tweet("asdf", LocalDate.now().minusMonths(1), user1);
        Tweet tweet2 = new Tweet("hghghg", LocalDate.now(), user1);

        tweetRepository.save(tweet1);
        tweetRepository.save(tweet2);

        List<Tweet> expectedTweets = Stream.of(tweet1, tweet2).collect(Collectors.toList());

        System.out.println(gson.toJson(expectedTweets));
        mockMvc.perform(get(URL_TWEETS_FOR_USER, user1.getId()))
                .andExpect(content().json(gson.toJson(expectedTweets)));
    }

    //DATE SERIALIZATION
    @Test
    public void testGetTweetsOnAParticularDate() throws Exception {
        User user1 = new User("m", "m", "M@m.com");
        userRepository.save(user1);

        Tweet tweet1 = new Tweet("asdf", LocalDate.now().minusMonths(1), user1);
        Tweet tweet2 = new Tweet("hghghg", LocalDate.now(), user1);

        tweetRepository.save(tweet1);
        tweetRepository.save(tweet2);

        List<Tweet> expectedTweets = Stream.of(tweet2).collect(Collectors.toList());

        mockMvc.perform(get(URL_GET_TWEETS_FOR_USER_ON_DATE, user1.getId()).param("date", "2019-08-15"))
                .andExpect(content().json(gson.toJson(expectedTweets)));
    }

    @Test
    public void testUpdateUserPassword() throws Exception {
        User user1 = new User("m", "m", "M@m.com");
        userRepository.save(user1);

        PasswordsDto passwordsDto = new PasswordsDto("m", "novom");

        User expectedUser = new User(1L, "m", "novom", "M@m.com", new ArrayList<>());

        mockMvc.perform(put(URL_USER_BY_ID, user1.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(gson.toJson(passwordsDto)))
                .andExpect(content().json(gson.toJson(expectedUser)));
    }

    @Test
    public void testDeleteUser() throws Exception {
        User userToBeDeleted = new User("m", "m", "m@m.com");
        userRepository.save(userToBeDeleted);

        mockMvc.perform(delete(URL_USER_BY_ID, userToBeDeleted.getId()));

        assertThat(userRepository.findById(userToBeDeleted.getId())).isNull();
    }

    @Test
    @Transactional
    public void testDeleteTweetsForUser() throws Exception {
        User user1 = new User("m", "m", "M@m.com");
        userRepository.save(user1);

        Tweet tweet1 = new Tweet("asdf", LocalDate.now().minusMonths(1), user1);
        Tweet tweet2 = new Tweet("hghghg", LocalDate.now(), user1);

        tweetRepository.save(tweet1);
        tweetRepository.save(tweet2);

        mockMvc.perform(delete(URL_TWEETS_FOR_USER, user1.getId()));

        assertThat(userRepository.findById(user1.getId()).get().getTweets()).isNull();
    }
}

