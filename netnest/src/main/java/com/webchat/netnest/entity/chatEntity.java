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
@Table(name = "chat_ID")
public class chatEntity {

    @Id
    @GeneratedValue
    private int chatID;

    @ManyToMany
    @JoinTable(name = "chatUser", joinColumns = @JoinColumn(name = "chatID"), inverseJoinColumns = @JoinColumn(name = "userChat"))
    private List<userEntity> userChat;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name = "chat_mess", joinColumns = @JoinColumn(name = "chatID"), inverseJoinColumns =  @JoinColumn(name = "messId"))
    private List<messageEntity> messageEntities;

    @ManyToMany
    @JoinTable(name = "chatReader", joinColumns = @JoinColumn(name = "chatID"), inverseJoinColumns = @JoinColumn(name = "userReader"))
    private List<userEntity> userReaded;

    private int countUser;

    private String chatName;

    @ManyToOne
    private userEntity createBy;

    @Column(name = "dateLastMess")
    private Date dateLastMess;



}

