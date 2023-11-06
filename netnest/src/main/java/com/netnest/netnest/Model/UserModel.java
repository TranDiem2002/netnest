package com.netnest.netnest.Model;

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
public class UserModel {

    private Long id;
    private int userId;
    private String email;
    private String fullName;
    private String Gender;
    private Date dateOfBirth;
    private String phone;
    private String userName;
    private String passWord;



}
