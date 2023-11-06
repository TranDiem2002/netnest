package com.netnest.netnest.Repository.Entity;


import com.netnest.netnest.Model.ImageModel;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "userId")
    private int user_id;
    @Column(name = "email")
    private String email;
    @Column(name = "fullName")
    private String fullname;
    @Column(name = "Gender")
    private String gender;
    @Column(name = "dateOfBirth")
    private Date dateofbirth;
    @Column(name = "phone")
    private String phone;
    @Column(name = "userName")
    private String username;
    @Column(name = "passWord")
    private String password;

//    @OneToMany
//    @JoinColumn(name = "image_id")
//    private ImageModel imageModel;
}
