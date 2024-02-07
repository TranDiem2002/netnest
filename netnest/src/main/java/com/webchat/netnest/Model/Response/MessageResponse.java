package com.webchat.netnest.Model.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {


    private int Id;

    private String message;

    private int createById;

    private String createByUserName;

    private String createDate;

    private Boolean statusMess;

}
