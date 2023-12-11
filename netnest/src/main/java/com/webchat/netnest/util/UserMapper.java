package com.webchat.netnest.util;

import com.webchat.netnest.Model.RegisterRequest;
import com.webchat.netnest.entity.UserEntity;
import org.modelmapper.ModelMapper;

public class UserMapper {

    ModelMapper modelMapper = new ModelMapper();

    public UserEntity convertToEntity(RegisterRequest user){
        return modelMapper.map(user, UserEntity.class);
    }
}
