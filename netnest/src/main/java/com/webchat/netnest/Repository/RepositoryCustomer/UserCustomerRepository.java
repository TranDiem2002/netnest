package com.webchat.netnest.Repository.RepositoryCustomer;

import com.webchat.netnest.entity.imageEntity;
import com.webchat.netnest.entity.userEntity;

import java.util.List;

public interface UserCustomerRepository {

    List<userEntity> findEmailByEmail(String email);
    List<userEntity> findUserByUserName(String username);

    List<userEntity> findUserName(String username);

//    List<userEntity> findFollowing(String username);

    void saveFollowing(userEntity user, userEntity userFollowing);

    int countFollowing(int userId);
    int countFollowers(int userId);

    List<userEntity> following(int userId);
    List<userEntity> followers(int userId);

    void updateDetailInformation(userEntity user);

    void updateAvatar(imageEntity image, int userId);


}