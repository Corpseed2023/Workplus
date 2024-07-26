package com.example.workplus.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_by")
    private Long createdBy;

    @CreatedDate
    @CreationTimestamp
    @Column(name = "creation_date")
    private Date creationDate;

    private Long createBy;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    private Long updateBy;

    private String subject;

    private String description;

    // When user raises a ticket, it should be marked as unresolved which is FALSE. After completion, it can be true.
    @Column(name = "issue_status")
    private boolean issueStatus = false;

    private String resolution;

    @Column(name = "is_enable", columnDefinition = "tinyint(1) default 1")
    @Comment(value = "1 : Active, 0 : Inactive")
    private boolean isEnable = true;

    @Column(name = "resolution_by")
    private Long resolutionBy;

    @Column(name = "resolution_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date resolutionDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;



}