package com.webchat.netnest.Model.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatStatus {


    private int chatID;

    private String message;

    private String createDateMess;

    private boolean statusChat;
}
