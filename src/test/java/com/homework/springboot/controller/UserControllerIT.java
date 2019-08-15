package com.homework.springboot.controller;

import com.google.gson.Gson;
import com.homework.springboot.model.User;
import com.homework.springboot.service.TweetService;
import com.homework.springboot.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIT {

    private static final String URL_USER_SAVE = "/users";

    private MockMvc mockMvc;

    private Gson gson;

    @Autowired
    private UserService userService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        gson = new Gson();
    }

    @Test
    public void testSaveUser() throws Exception {
        User user = new User()
                .withEmail("mare@mare.com")
                .withPassword("mare")
                .withUsername("mare");

        String userJsonString = gson.toJson(user);

        mockMvc.perform(post(URL_USER_SAVE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(userJsonString)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        //dali ovde treba da praime nesho so repository sporebda megju toa sho sme dobile i toa sho ochekuvame?

    }
}
