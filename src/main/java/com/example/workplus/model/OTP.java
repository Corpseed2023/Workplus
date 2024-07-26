package com.example.workplus.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OTP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "otp")
    private String otp;


    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;


//    @Column(length = 1,name="is_enable",columnDefinition = "tinyint(1) default 1")
//    @Comment(value = "1 : Active, 0 : Inactive")
//    private boolean isUsed;

    @Column(name = "count")
    private Integer count = 0;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
