package com.webchat.netnest.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "message")
@EntityListeners(AuditingEntityListener.class)
public class messageEntity {

    @Id
    @ManyToOne
    @JoinColumn(name = "chatID")
    private chatEntity chatID;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createBy")
    private userEntity createBy;

    @Column(name = "createDate")
    @CreatedDate
    private Date createDate;


    @Column(name = "modifiedDate")
    @LastModifiedDate
    private Date modifiedDate;
}
