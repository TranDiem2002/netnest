package com.webchat.netnest.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {

    private int userId;
    private String userName;
    private String fullName;
    private String base64Image;
    private String statusFollow;

}
