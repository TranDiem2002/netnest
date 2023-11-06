package com.netnest.netnest.Service.Impl;

import com.netnest.netnest.Model.UserModel;
import com.netnest.netnest.Repository.Entity.UserEntity;
import com.netnest.netnest.Repository.UserRepository;
import com.netnest.netnest.Service.UserService;
import com.netnest.netnest.until.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserMapper userMapper;
    @Autowired
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserModel saveUser(UserModel user) {
        UserEntity userEntity = userMapper.covertToEntity(user);
        UserEntity saveUser = userRepository.save(userEntity);
        return userMapper.covertToModel(saveUser);
    }
}
