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
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "post")
@EntityListeners(AuditingEntityListener.class)
public class postEntity {

    @Id
    @Column(name = "postID")
    private int postID;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "createDate")
    @CreatedDate
    private Date createDate;

    @ManyToOne
    private userEntity createBy;

    @Column(name = "modifiedDate")
    @LastModifiedDate
    private Date modifiedDate;


    @ManyToMany
    @JoinTable(name = "comment_post", joinColumns = @JoinColumn(name = "post"), inverseJoinColumns = @JoinColumn(name = "comment"))
    private List<commentEntity> commentEntities;

    @ManyToMany
    @JoinTable(name = "like_post", joinColumns = @JoinColumn(name = "post"), inverseJoinColumns = @JoinColumn(name = "user_like"))
    private List<userEntity> user;

//    @OneToMany
//    private List<videoEntity> videoEntity;

    @ManyToMany
    @JoinTable(name = "image_post", joinColumns = @JoinColumn(name = "post"), inverseJoinColumns = @JoinColumn(name = "image"))
    private List<imageEntity> image;

    @ManyToMany
    @JoinTable(name = "video_post", joinColumns = @JoinColumn(name = "post"), inverseJoinColumns = @JoinColumn(name = "video"))
    private List<videoEntity> video;
}
