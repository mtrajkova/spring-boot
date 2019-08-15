package com.homework.springboot.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.homework.springboot.model.serialization.JsonDateDeserializer;
import com.homework.springboot.model.serialization.JsonDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Wither;

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
@Wither
public class Tweet {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank
    private String content;

//    @JsonDeserialize(using = JsonDateDeserializer.class)
//    @JsonSerialize(using = JsonDateSerializer.class)
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
