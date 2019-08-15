package com.homework.springboot.service;

import com.homework.springboot.exceptions.UserAlreadyExists;
import com.homework.springboot.exceptions.UserDoesNotExist;
import com.homework.springboot.model.Tweet;
import com.homework.springboot.model.User;
import com.homework.springboot.model.dto.PasswordsDto;
import com.homework.springboot.repository.UserRepository;
import com.homework.springboot.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

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

    @Test
    public void testGetAllUsersThatHaveTweetedLastMonth() {
        List<User> users = new ArrayList<>();
        List<Tweet> lastMonthTweets = new ArrayList<>();

        User user1 = new User(1L, "mare", "mare", "mare@mare.com", new ArrayList<>());

        lastMonthTweets.add(new Tweet(1L, "asdf", LocalDate.now(), user1));
        lastMonthTweets.add(new Tweet(2L, "dfghdfhg", LocalDate.now().minusMonths(1), user1));

        user1.setTweets(lastMonthTweets);
        users.add(user1);

        when(userService.getAllUsersThatHaveTweetedLastMonth()).thenReturn(users);

        List<User> actualUsers = userService.getAllUsersThatHaveTweetedLastMonth();
        assertThat(users).isEqualTo(actualUsers);
    }

    @Test
    public void testUpdatePassword() throws UserDoesNotExist {
        Optional<User> user = Optional.of(new User(1L, "mare", "mare", "mare@mare.com", new ArrayList<>()));
        doReturn(user).when(userRepository).findById(anyLong());
        //when(userRepository.findById(anyLong())).thenReturn(user);

        User actualUpdatedUser = userService.updatePassword(1L, new PasswordsDto("mare", "novomare"));
        User expectedUser = new User("mare", "novomare", "mare@mare.com");

        assertThat(expectedUser.getPassword()).isSameAs(actualUpdatedUser.getPassword());
    }

    @Test
    public void deleteUser() {
        User user = new User("mare", "mare", "mare@mare.com");
        userRepository.save(user);

    }
}
