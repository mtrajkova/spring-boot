package com.homework.springboot.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String username;

    @Value("${user.password}")
    private String password;

    @Email
    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Tweet> tweets;

    public List<Tweet> lastMonthsTweets() {
        List<Tweet> lastMonthTweets = new ArrayList<>();

        for (Tweet tweet : tweets) {
            if (tweet.checkIsLastMonth()) {
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
