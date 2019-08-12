package com.homework.springboot.model;

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
@Setter
@Getter
public class Tweet {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @NotBlank
    private String content;
    private Date creationDate;
    @ManyToOne
    @NotNull
    private User userId;

    public boolean isLastMonth() {
        Date now = new Date();
        LocalDate localDateNow = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localDateCreation = creationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return localDateNow.minusMonths(1).getMonth().equals(localDateCreation.getMonth());
    }

}
