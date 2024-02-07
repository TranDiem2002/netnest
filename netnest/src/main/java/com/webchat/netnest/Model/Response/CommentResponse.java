package com.webchat.netnest.Model.Response;

import com.webchat.netnest.Model.UserModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private int commentID;
    private UserModel user;
    private String comment;
    private String createDate;

    private Date modifiedDate;

    private int countLike;
    private boolean statusLike;

}
