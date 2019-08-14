package com.homework.springboot.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Tweet {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank
    private String content;

    private LocalDate creationDate;

    @ManyToOne
    @JsonBackReference
    private User user;

    public Tweet(@NotBlank String content, LocalDate creationDate, User user) {
        this.content = content;
        this.creationDate = creationDate;
        this.user = user;
    }

    public boolean checkIsLastMonth() {
        LocalDate now = LocalDate.now();

        return now.minusMonths(1).getMonth().equals(creationDate.getMonth());
    }

}
