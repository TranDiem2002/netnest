package com.webchat.netnest.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class chatUsersModel {

    private int chatID;


    private List<UserModel> userOther;


    private String message;

    private String chatName;

    private String createDateMess;

    private Boolean statusChat;

}
