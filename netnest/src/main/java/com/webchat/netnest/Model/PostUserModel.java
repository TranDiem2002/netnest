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
public class PostUserModel {

    private int postID;

    private String content;

    private List<String> base64Image;

    private List<String> base64video;

    private int countLike;

    private int countComments;
}
