package com.netnest.netnest.until;

import com.netnest.netnest.Model.UserModel;
import com.netnest.netnest.Repository.Entity.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class UserMapper {

    @Autowired
    private ModelMapper modelMapper;

    public UserModel covertToModel (UserEntity user){
        return modelMapper.map(user, UserModel.class);
    }
    public UserEntity covertToEntity (UserModel user){
        return modelMapper.map(user, UserEntity.class);
    }
}
