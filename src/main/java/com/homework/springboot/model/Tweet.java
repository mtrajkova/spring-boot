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

    private Date creationDate = new Date();

    @ManyToOne
    @JsonBackReference
    private User user;

    public boolean checkIsLastMonth() {
        Date now = new Date();
        LocalDate localDateNow = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localDateCreation = creationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return localDateNow.minusMonths(1).getMonth().equals(localDateCreation.getMonth());
    }

}
