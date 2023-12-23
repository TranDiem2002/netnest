package com.webchat.netnest.util;

import com.webchat.netnest.Model.PostDetail;
import com.webchat.netnest.Model.PostModel;
import com.webchat.netnest.Model.Response.CommentResponse;
import com.webchat.netnest.Model.UserModel;
import com.webchat.netnest.entity.commentEntity;
import com.webchat.netnest.entity.imageEntity;
import com.webchat.netnest.entity.postEntity;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

public class PostMapper {

    ModelMapper modelMapper = new ModelMapper();

    private ImageMapper imageMapper;

    private UserMapper userMapper;

    private  CommentMapper commentMapper;

    public PostMapper() {
        this.imageMapper = new ImageMapper();
        this.userMapper = new UserMapper();
        this.commentMapper = new CommentMapper();
    }

    public postEntity convertToEntity(PostModel postModel){
        return modelMapper.map(postModel, postEntity.class);
    }

    public List<String> convertImage(List<imageEntity> imageEntities){
        List<String> images = new ArrayList<>();
        if(imageEntities != null){
            for (imageEntity image: imageEntities){
                String image1 = imageMapper.convertBase64(image);
                images.add(image1);
            }
        }

        return images;
    }

    public PostModel convertToModel(postEntity post){
        PostModel postModel = modelMapper.map(post, PostModel.class);
        List<String> images = this.convertImage(post.getImage());
        UserModel userModel = userMapper.convertToModel(post.getCreateBy());
        String avatarBase = imageMapper.convertBase64(post.getCreateBy().getImage());
        userModel.setBase64Image(avatarBase);
        postModel.setBase64Image(images);
        postModel.setCreateBy(userModel);
        return postModel;
    }


    public PostDetail convertToDetail(postEntity post, int countLikes){
        PostDetail postDetail = modelMapper.map(post, PostDetail.class);
        List<String> images = this.convertImage(post.getImage());
        UserModel userModel = userMapper.convertToModel(post.getCreateBy());
        String avatarBase = imageMapper.convertBase64(post.getCreateBy().getImage());

        //comment
        List<commentEntity> commentEntities = post.getCommentEntities();
        List<CommentResponse> commentResponses = new ArrayList<>();
        if(commentEntities != null){
            for (commentEntity comment : commentEntities){
                CommentResponse commentResponse = new CommentResponse();
                commentResponse = commentMapper.convertToModel(comment);
                commentResponses.add(commentResponse);
            }
        }

        userModel.setBase64Image(avatarBase);
        postDetail.setBase64Image(images);
        postDetail.setCreateBy(userModel);
        postDetail.setCountLike(countLikes);
        postDetail.setComments(commentResponses);
        return postDetail;
    }
}
