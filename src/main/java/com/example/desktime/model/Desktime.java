package com.example.desktime.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;


@Entity(name = "desktime")
@Getter
@Setter
@NoArgsConstructor
public class Desktime {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "screenshot")
    private String screenshot;

    @Column(name = "process")
    private String process;

    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @Column(name = "time_convention")
    private String timeConvention;

    @Column(name = "`currentDate`")
    @Temporal(TemporalType.TIMESTAMP)
    private Date currentDate;


}
