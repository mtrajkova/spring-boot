package com.homework.springboot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @NotBlank
    private String username;
    private String password;
    @Email
    private String email;
    @OneToMany(mappedBy = "userId", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Tweet> tweets;

    public List<Tweet> getLastMonthsTweets() {
        List<Tweet> lastMonthTweets = new ArrayList<>();

        for (Tweet tweet : tweets) {
            if (tweet.isLastMonth()) {
                lastMonthTweets.add(tweet);
            }
        }

        return lastMonthTweets;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
