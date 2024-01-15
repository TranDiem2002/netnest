package com.webchat.netnest.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comment")
@EntityListeners(AuditingEntityListener.class)
public class commentEntity {

    @Id
    @GeneratedValue
    @Column(name = "commentID")
    private int commentID;

    @ManyToOne
    private userEntity user;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "createDate")
    private Date createDate;

    @Column(name = "modifiedDate")
    private Date modifiedDate;

    @ManyToMany
    @JoinTable(name = "comment_like", joinColumns = @JoinColumn(name = "comment"), inverseJoinColumns = @JoinColumn(name = "user"))
    private List<userEntity> userLikes;


}
