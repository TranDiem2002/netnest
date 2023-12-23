package com.webchat.netnest.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {

    private String comment;
    private Date createDate;

    private Date modifiedDate;

}
