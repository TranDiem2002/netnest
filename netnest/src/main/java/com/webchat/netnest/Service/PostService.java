package com.webchat.netnest.Service;

import com.webchat.netnest.Model.*;
import com.webchat.netnest.Model.Request.CommentRequest;
import com.webchat.netnest.Model.Response.CommentResponse;

import java.util.List;

public interface PostService {
    PostModel createImagePost(ImageModel images, int postId);

    PostModel createPost(PostModel postModel, String userEmail);

    PostModel getPost(int postId);

    PostModel addLikes(String userEmail, int postId);

    List<UserModel> getUserLikes(int postId);

    PostModel addComment(int postId, CommentRequest commentRequest, String userEmail);

    List<CommentResponse> getComment(int postId);

    PostDetail getDetailPost(int postId);

    void deletePost(int postId);

}

