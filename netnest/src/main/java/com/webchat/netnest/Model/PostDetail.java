package com.webchat.netnest.Model;

import com.webchat.netnest.Model.Response.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostDetail {
    private int postID;

    private String content;

    private List<String> base64Image;
    private List<String> base64Video;
    private int countLike;

    private UserModel createBy;
    private String CreateDate;
    private List<CommentResponse> comments;
    private boolean likeStatus;
    private boolean followStatus;
}
