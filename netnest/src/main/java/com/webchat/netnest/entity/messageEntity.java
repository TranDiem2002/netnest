package com.webchat.netnest.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    @GeneratedValue
    private int Id;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createBy")
    private userEntity createBy;

    @Column(name = "createDate")
    private Date createDate;


    @Column(name = "modifiedDate")
    private Date modifiedDate;

    @Column(name = "statusMess")
    private Boolean statusMes;
}
