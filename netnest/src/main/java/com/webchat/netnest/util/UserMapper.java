package com.webchat.netnest.util;

import com.webchat.netnest.Model.RegisterRequest;
import com.webchat.netnest.Model.UserModel;
import com.webchat.netnest.Model.UserProfileModel;
import com.webchat.netnest.entity.imageEntity;
import com.webchat.netnest.entity.userEntity;
import org.modelmapper.ModelMapper;

import java.sql.Blob;
import java.sql.SQLException;

public class UserMapper {

    ModelMapper modelMapper = new ModelMapper();

    public userEntity convertToEntity(RegisterRequest user){
        return modelMapper.map(user, userEntity.class);
    }

    public UserProfileModel convertToProfileModel(userEntity user, String image){
        UserProfileModel userProfileModel = modelMapper.map(user, UserProfileModel.class);
        userProfileModel.setBase64Image(image);
        return userProfileModel;
    }


    public userEntity convertToEntity(UserProfileModel user){
        return modelMapper.map(user, userEntity.class);
    }

    public UserModel convertPToModel(UserProfileModel user){
        UserModel userModel = modelMapper.map(user, UserModel.class);
        userModel.setBase64Image(user.getBase64Image());
        return userModel;
    }

    public userEntity convertMToEntity  (UserModel userModel)
    {
        return modelMapper.map(userModel, userEntity.class);
    }

    public UserModel convertToModel(userEntity user){
        UserModel userModel = modelMapper.map(user, UserModel.class);
        imageEntity avatar = user.getImage();
        Blob blob = avatar.getImage();
        String base64Image = null;
        try {
            base64Image = java.util.Base64.getEncoder().encodeToString(blob.getBytes(1, (int) blob.length()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        userModel.setBase64Image(base64Image);
        return userModel;
    }
}
