package com.webchat.netnest.Service;

import com.webchat.netnest.Model.ImageModel;
import com.webchat.netnest.Model.UserModel;
import com.webchat.netnest.Model.UserProfileModel;

import java.sql.SQLException;
import java.util.List;


public interface UserService {

//    UserProfileModel createUser(UserProfileModel userModel);

    List<UserModel> searchUser(String username) throws SQLException;

    UserProfileModel searchDetailUser(String username);

    void saveFollowing(int userId, String userName);

    List<UserModel> following(int userId);
    List<UserModel> followers(int userId);

    void updateInformation(UserProfileModel userProfileModel);

    void updateImage (ImageModel image, int userId);

    List<UserModel> suggestFriends(String userEmail);

}
