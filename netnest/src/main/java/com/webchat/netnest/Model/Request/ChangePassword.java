package com.webchat.netnest.Model.Request;


import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePassword {

    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}
