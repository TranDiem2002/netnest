package com.webchat.netnest.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileModel {

//    private Long id;

    private int userId;

    private String email;

    private String fullName;

    private String gender;

    private Date dateOfBirth;

    private String phone;

    private String base64Image;

    private String userName;

    private Date createDate;

    private Date modifiedDate;

    private int countfollowing;

    private int countfollowers;

}