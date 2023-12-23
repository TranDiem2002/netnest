package com.webchat.netnest.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "requestMessage")
public class requestMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private userEntity createBy;

    @ManyToOne
    private userEntity userReceive;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "date")
    private Date createDate;
}

