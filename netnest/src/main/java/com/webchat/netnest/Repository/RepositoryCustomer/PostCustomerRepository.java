package com.webchat.netnest.Repository.RepositoryCustomer;

import com.webchat.netnest.entity.commentEntity;
import com.webchat.netnest.entity.imageEntity;
import com.webchat.netnest.entity.postEntity;
import com.webchat.netnest.entity.userEntity;

import java.util.List;

public interface PostCustomerRepository {

    postEntity updateImagePost(imageEntity image, int postId);

    postEntity addLike(int postId, int userId);

    int countLikes(int postID);

    List<userEntity> getUserLikes(int postId);

    postEntity addComment(int postId, commentEntity comment);

    int countComment(int postId);

    List<commentEntity> getComment(int postId);

}