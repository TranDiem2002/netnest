package com.webchat.netnest.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;


@Data
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class userEntity implements UserDetails {

    @Id
    @GeneratedValue
    private int userId;

    @Column(name = "email")
    private String email;

    @Column(name = "fullName")
    private String fullName;

    @Column(name = "Gender")
    private String Gender;

    @Column(name = "dateOfBirth")
    private Date dateOfBirth;

    @Column(name = "phone")
    private String phone;

    @Column(name = "userName")
    private String userName;

    @Column(name = "passWord")
    private String passWord;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    private imageEntity image;

    @OneToMany(mappedBy = "user")
    private List<status_user> time;

    @Column(name = "createDate")
    private Date createDate;


    @Column(name = "modifiedDate")
    private Date modifiedDate ;

    @ManyToMany
    @JoinTable(name = "following", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "following_id"))
    private List<userEntity> following;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @OneToMany(mappedBy = "createBy")
    private List<messageEntity> createBy;

    @OneToMany(mappedBy = "createBy")
    private List<postEntity> postEntities;

    @OneToMany(mappedBy = "user")
    private List<commentEntity> commentEntities;

    @OneToMany(mappedBy = "createBy")
    private List<requestMessage> requestMessage;

    @OneToMany(mappedBy = "userReceive")
    private List<requestMessage> requestReceive;

    @OneToMany(mappedBy = "createBy")
    private List<chatEntity> chat;

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.getRole().name()));
    }

    @Override
    public String getPassword() {
        return passWord;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
