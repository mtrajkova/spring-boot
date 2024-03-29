package com.homework.springboot.service;

import com.homework.springboot.exceptions.UserAlreadyExists;
import com.homework.springboot.exceptions.UserDoesNotExist;
import com.homework.springboot.model.Tweet;
import com.homework.springboot.model.User;
import com.homework.springboot.model.dto.PasswordsDto;
import com.homework.springboot.repository.TweetRepository;
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
import org.springframework.boot.test.context.SpringBootTest;

import java.security.cert.CollectionCertStoreParameters;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    TweetRepository tweetRepository;

    private UserServiceImpl userService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private SimpleDateFormat dateFormat;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userService = new UserServiceImpl(userRepository, tweetRepository);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
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
    public void testGetAllUsersThatHaveTweetedLastMonth() throws ParseException {
        List<User> expectedUsers = new ArrayList<>();

        User user1 = new User(1L, "mare", "mare", "mare@mare.com", new ArrayList<>());
        User user2 = new User(2L, "eci", "peci", "pec@a.com", new ArrayList<>());

        Tweet tweet1 = new Tweet(1L, "asdf", new Date(), user1);
        Tweet tweet2 = new Tweet(2L, "dfghdfhg", dateFormat.parse("2019-07-02"), user1);

        user1.setTweets(Stream.of(tweet1, tweet2).collect(Collectors.toList()));
        user2.setTweets(Stream.of(tweet1).collect(Collectors.toList()));

        expectedUsers.add(user1);

        when(userRepository.findAll()).thenReturn(Stream.of(user1, user2).collect(Collectors.toList()));

        List<User> actualUsers = userService.getAllUsersThatHaveTweetedLastMonth();
        assertThat(expectedUsers).isEqualTo(actualUsers);
    }

    @Test
    public void testUpdatePassword() throws UserDoesNotExist {
        User user = new User(1L, "mare", "mare", "mare@mare.com", new ArrayList<>());

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        User actualUpdatedUser = userService.updatePassword(1L, new PasswordsDto("mare", "novomare"));
        User expectedUser = new User("mare", "novomare", "mare@mare.com");

        assertThat(expectedUser.getPassword()).isSameAs(actualUpdatedUser.getPassword());
    }

    @Test
    public void testGetById() throws UserDoesNotExist {
        User user = new User(1L, "mare", "mare", "mare@mare.com", new ArrayList<>());

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        User actualUser = userService.getById(user.getId());

        assertThat(user.getUsername()).isSameAs(actualUser.getUsername());
    }

    @Test
    public void testDeleteUser() throws UserDoesNotExist {
        User user = new User(1L, "mare", "mare", "mare@mare.com", new ArrayList<>());

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        userService.delete(user.getId());

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    public void testDeleteUserShouldThrowUserDoesNotExist() throws UserDoesNotExist {
        User user = new User(1L, "mare", "mare", "mare@mare.com", new ArrayList<>());

        expectedException.expect(UserDoesNotExist.class);

        userService.delete(user.getId());

    }

    @Test
    public void testGetTweetsForUser() throws UserDoesNotExist {
        User user = new User(1L, "mare", "mare", "mare@mare.com", new ArrayList<>());
        Tweet tweet = new Tweet();

        user.setTweets(Stream.of(tweet).collect(Collectors.toList()));

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertThat(Stream.of(tweet).collect(Collectors.toList())).isEqualTo(userService.getTweetsForUser(user.getId()));
    }

    @Test
    public void testGetTweetsOnAParticularDate() throws UserDoesNotExist {
        User user = new User(1L, "mare", "mare", "mare@mare.com", new ArrayList<>());

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        List<Tweet> tweets = Stream.of(new Tweet()).collect(Collectors.toList());

        when(tweetRepository.findByCreationDateAndUserId(any(), anyLong())).thenReturn(tweets);

        List<Tweet> actualResult = userService.getTweetsOnAParticularDate(user.getId(), LocalDate.now());

        assertThat(tweets).isEqualTo(actualResult);
    }

    @Test
    public void testDeleteTweetsForUser() throws UserDoesNotExist {
        User user = new User(1L, "mare", "mare", "mare@mare.com", new ArrayList<>());
        Tweet tweet = new Tweet();

        user.setTweets(Stream.of(tweet).collect(Collectors.toList()));

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        when(tweetRepository.findByUserId(any())).thenReturn(Stream.of(tweet).collect(Collectors.toList()));

        userService.deleteTweetsForUser(user.getId());

        verify(tweetRepository, times(1)).delete(tweet);
    }
}
