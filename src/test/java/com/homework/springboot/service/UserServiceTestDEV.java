package com.homework.springboot.service;

import com.homework.springboot.exceptions.UserAlreadyExists;
import com.homework.springboot.model.Tweet;
import com.homework.springboot.model.User;
import com.homework.springboot.repository.TweetRepository;
import com.homework.springboot.repository.UserRepository;
import com.homework.springboot.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTestDEV {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSave() throws UserAlreadyExists {
        User user = new User();
        user.setEmail("mare@mare.com");
        user.setUsername("mare");
        user.setPassword("mare");

        when(userRepository.save(any(User.class))).thenReturn(user);

        User userCreated = userService.save(user);

        assertThat(userCreated.getUsername()).isSameAs(user.getUsername());
    }
}
