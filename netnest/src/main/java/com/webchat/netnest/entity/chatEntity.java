package com.webchat.netnest.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chat_ID")
@EntityListeners(AuditingEntityListener.class)
public class chatEntity {

    @Id
    @Column(name = "chatID")
    private int chatID;

    @ManyToOne
    private userEntity user1;

    @ManyToOne
    private userEntity user2;

    @OneToMany(mappedBy = "chatID")
    private Set<messageEntity> messageEntities;

}

