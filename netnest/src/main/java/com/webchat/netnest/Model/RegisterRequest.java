package com.webchat.netnest.Model;


import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    private String email;
    private String fullName;

    private String userName;

    private String passWord;


}
