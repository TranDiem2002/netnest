package com.webchat.netnest.Model.Request;


import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {

    private String email;
    String passWord;
    private Date dateLogin;
}
