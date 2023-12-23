package com.webchat.netnest.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "story")
@EntityListeners(AuditingEntityListener.class)
public class storyEntity {

    @Id
    private int storyID;

    @ManyToOne
    private userEntity createBy;

    @Column(name = "createDate")
    @CreatedDate
    private Date createDate;

    @OneToOne
    private imageEntity image;

    @OneToOne
    private  videoEntity videoEntities;

}
