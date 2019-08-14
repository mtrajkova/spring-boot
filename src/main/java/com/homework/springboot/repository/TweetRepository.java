package com.homework.springboot.repository;

import com.homework.springboot.model.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {
    List<Tweet> findByCreationDateAndUserId(LocalDate date, Long id);

    List<Tweet> findByUserId(Long id);
}
