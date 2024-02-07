package com.webchat.netnest.util;

import com.webchat.netnest.Model.Request.CommentRequest;
import com.webchat.netnest.Model.Response.CommentResponse;
import com.webchat.netnest.Model.UserModel;
import com.webchat.netnest.entity.commentEntity;
import com.webchat.netnest.entity.imageEntity;
import com.webchat.netnest.entity.userEntity;
import org.modelmapper.ModelMapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentMapper {

    ModelMapper modelMapper = new ModelMapper();

    private ImageMapper imageMapper;

    private  UserMapper userMapper;

    public CommentMapper() {
        this.imageMapper = new ImageMapper();
        this.userMapper = new UserMapper();
    }

    public String convertTimetoString(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(date);
        return formattedDate;
    }

    public List<String> convetImage(List<imageEntity> imageEntities){
        List<String> images = new ArrayList<>();
        for (imageEntity image: imageEntities){
            String image1 = imageMapper.convertBase64(image);
            images.add(image1);
        }
        return images;
    }

    public CommentResponse convertToModel(commentEntity comment){
        userEntity user = comment.getUser();
        UserModel userModel = userMapper.convertToModel(user);
        CommentResponse commentResponse = modelMapper.map(comment, CommentResponse.class);
        userModel.setBase64Image(imageMapper.convertBase64(user.getImage()));
        commentResponse.setUser(userModel);
        commentResponse.setCreateDate(convertTimetoString(comment.getCreateDate()));
        return commentResponse;
    }

    public commentEntity convertToEntity(CommentRequest commentRequest){
        return modelMapper.map(commentRequest, commentEntity.class);
    }
}