package com.webchat.netnest.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notice")
public class noticeEntity {


    @Id
    @GeneratedValue
    private int noticeId;

    @Column(name = "content", columnDefinition = "TEXT")
    private String contentNotice;

    @Column(name = "createDate")
    private Date createNotice;

    @ManyToMany
    @JoinTable(name = "user_notice", joinColumns = @JoinColumn(name = "notice"), inverseJoinColumns = @JoinColumn(name = "user"))
    private List<userEntity> userEntities;
}
