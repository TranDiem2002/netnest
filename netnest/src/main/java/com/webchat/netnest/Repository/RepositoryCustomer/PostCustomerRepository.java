package com.webchat.netnest.Repository.RepositoryCustomer;

import com.webchat.netnest.entity.*;

import java.util.Date;
import java.util.List;

public interface PostCustomerRepository {

    postEntity updateImagePost(imageEntity image, int postId);

    postEntity updateVideoPost(videoEntity video, int postId);

    postEntity addLike(int postId, int userId);

    postEntity disLike(int postId, int userId);

    int countLikes(int postID);

    List<userEntity> getUserLikes(int postId);

    postEntity addComment(int postId, commentEntity comment);

    int countComment(int postId);

    List<commentEntity> getComment(int postId);

    int countLikesComment(int comment);

    List<Integer> getPostProfile(int userCreateBy);

    List<Integer> getPostHome(Date date);
}