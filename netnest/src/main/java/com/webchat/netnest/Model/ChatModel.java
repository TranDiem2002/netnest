package com.webchat.netnest.Model;

import com.webchat.netnest.Model.Response.MessageResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatModel {

    private int chatID;


    private List<UserModel> userOther;


    private List<MessageResponse> message;

    private String chatName;

    private boolean statusChat;
}

