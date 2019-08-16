package com.homework.springboot.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Wither;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Wither
public class Tweet implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank
    private String content;

    //    @JsonDeserialize(using = JsonDateDeserializer.class)
//    @JsonSerialize(using = JsonDateSerializer.class)
    private Date creationDate;

    @ManyToOne
    @JsonBackReference
    private User user;

    public Tweet(@NotBlank String content, Date creationDate, User user) {
        this.content = content;
        this.creationDate = creationDate;
        this.user = user;
    }

    public boolean checkIsLastMonth() {
        LocalDate now = LocalDate.now();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(creationDate);

        System.out.println(calendar.getTime());
        System.out.println(calendar.get(calendar.MONTH));

        return now.minusMonths(1).getMonth().getValue() == calendar.get(calendar.MONTH)+1;
    }

}
