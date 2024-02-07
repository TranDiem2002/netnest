package com.webchat.netnest.Service;

import com.webchat.netnest.Model.ImageModel;
import com.webchat.netnest.Model.UserModel;
import com.webchat.netnest.Model.UserProfileModel;

import java.sql.SQLException;
import java.util.List;


public interface UserService {

//    UserProfileModel createUser(UserProfileModel userModel);

    List<UserModel> searchUser(String username, String userEmail) throws SQLException;

    UserProfileModel searchDetailUser(String username, String userEmail);

    UserProfileModel DetailUser(String username);

    void saveFollowing(int userId, String userName);

    void deleteFollowing(int userId, String userEmail);

    List<UserModel> following(int userId);
    List<UserModel> followers(int userId);

    List<UserModel> followingUser(int userId, String userEmail);
    List<UserModel> followersUser(int userId, String userEmail);

    void updateInformation(UserProfileModel userProfileModel);

    void updateImage (ImageModel image, int userId);

    List<UserModel> suggestFriends(String userEmail);

    UserModel getUser(String userEmail);

    boolean getUserActive(int userId);
}
