package com.webchat.netnest.Service.Impl;

import com.webchat.netnest.Model.ImageModel;
import com.webchat.netnest.Model.UserModel;
import com.webchat.netnest.Model.UserProfileModel;
import com.webchat.netnest.Repository.ImageRepository;
import com.webchat.netnest.Repository.RepositoryCustomer.Impl.UserCustomerRepositoryImpl;
import com.webchat.netnest.Repository.RepositoryCustomer.UserCustomerRepository;
import com.webchat.netnest.Repository.StatusRepository;
import com.webchat.netnest.Repository.UserRepository;
import com.webchat.netnest.Service.ImageService;
import com.webchat.netnest.Service.UserService;
import com.webchat.netnest.entity.Token;
import com.webchat.netnest.entity.imageEntity;
import com.webchat.netnest.entity.userEntity;
import com.webchat.netnest.util.ImageMapper;
import com.webchat.netnest.util.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

        @Autowired
        private UserRepository userRepository;


        @Autowired
        private UserCustomerRepository userCustomerRepository;

        @Autowired
        private ImageService imageService;

        private ImageMapper imageMapper;

        @Autowired
        private ImageRepository imageRepository;

        @Autowired
        private StatusRepository statusRepository;

        private UserMapper userMapper;

        public UserServiceImpl() {

            this.userMapper = new UserMapper();
            this.imageMapper = new ImageMapper();
            this.userCustomerRepository = new UserCustomerRepositoryImpl();
        }


        public String covertImage(Long imageId){
            String base64Image = null;
            Optional<imageEntity> image = imageService.findImageById(imageId);
            if (image.isPresent()) {
                imageEntity imageE = image.get();
                Blob imageB = imageE.getImage();
                // Chuyển đổi dữ liệu hình ảnh thành Base64
                try {
                    base64Image = java.util.Base64.getEncoder().encodeToString(imageB.getBytes(1, (int) imageB.length()));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            return base64Image;
        }


        // tim kiem theo userName
        public List<UserProfileModel> searchPUser(String username)  {
            List<userEntity> user1 = userCustomerRepository.findUserName(username);
            List<UserProfileModel> userP = new ArrayList<>();
            for (userEntity user : user1) {
                String base64Image = this.covertImage(user.getImage().getId());
                UserProfileModel userProfileModel = userMapper.convertToProfileModel(user, base64Image);
                userP.add(userProfileModel);
            } return userP;
        }

    public List<UserProfileModel> searchUserOther(String username, userEntity userEntity)  {
        List<userEntity> user1 = userCustomerRepository.findUserName(username);
        List<UserProfileModel> userP = new ArrayList<>();
        for (userEntity user : user1) {
            if (user.getUserId() == userEntity.getUserId()) {
                user1.remove(user);
            }
        }
        for (userEntity user : user1) {
                String base64Image = this.covertImage(user.getImage().getId());
                UserProfileModel userProfileModel = userMapper.convertToProfileModel(user, base64Image);
                userP.add(userProfileModel);
        } return userP;
    }

        // tifm kieems theo Email
    public List<UserProfileModel> SDetailUser(String userEmail)  {
        List<userEntity> user1 = userCustomerRepository.findEmailByEmail(userEmail);
        List<UserProfileModel> userP = new ArrayList<>();
        for (userEntity user : user1) {
            String base64Image = this.covertImage(user.getImage().getId());
            UserProfileModel userProfileModel = userMapper.convertToProfileModel(user, base64Image);
            userP.add(userProfileModel);
        } return userP;
    }

        @Override
        public List<UserModel> searchUser(String username, String userEmail)  {
            userEntity userEntity = userRepository.findByEmail(userEmail).get();
            List<UserProfileModel> users = this.searchUserOther(username, userEntity);
            List<UserModel> userModels = new ArrayList<>();
            for(UserProfileModel user : users){
                UserModel userModel = userMapper.convertPToModel(user);
                userModels.add(userModel);
            }
            return userModels;
        }

        @Override
        public UserProfileModel searchDetailUser(String username, String userEmail) {
            List<UserProfileModel> users = this.searchPUser(username);
            UserProfileModel userP = users.get(0);
            userEntity user = userRepository.findByEmail(userEmail).get();
            List<Integer> id = userCustomerRepository.checkFollow(userP.getUserId(), user.getUserId());
            if(!id.isEmpty()){
                userP.setStatusFollow(true);
            }
            int countFollowing = userCustomerRepository.countFollowing(userP.getUserId());
            int countFollowers = userCustomerRepository.countFollowers(userP.getUserId());
            userP.setCountfollowing(countFollowing);
            userP.setCountfollowers(countFollowers);
            return userP;
        }

    @Override
    public UserProfileModel DetailUser(String userEmail) {
        List<UserProfileModel> users = this.SDetailUser(userEmail);
        UserProfileModel userP = users.get(0);
        int countFollowing = userCustomerRepository.countFollowing(userP.getUserId());
        int countFollowers = userCustomerRepository.countFollowers(userP.getUserId());
        userP.setCountfollowing(countFollowing);
        userP.setCountfollowers(countFollowers);
        return userP;
    }

    @Override
        public void saveFollowing(int userId, String userName) {
           userEntity user = userRepository.findByUserName(userName).get();
            Optional<userEntity> userEntity = userRepository.findById(userId);
            userCustomerRepository.saveFollowing(userEntity.get(), user);
        }

    @Override
    public void deleteFollowing(int userId, String userEmail) {
        userEntity user = userRepository.findByEmail(userEmail).get();
        userEntity userFollowing = userRepository.findById(userId).get();
        List<userEntity> userFollowings = user.getFollowing();
        if(userFollowings.contains(userFollowing)){
            userFollowings.remove(userFollowing);
            user.setFollowing( userFollowings);
            userRepository.save(user);
        }
    }

    @Override
        public List<UserModel> following(int userId) {
            List<userEntity> users = userCustomerRepository.following(userId);
            List<UserModel> userFollowing = new ArrayList<>();
            for (userEntity user: users){
                UserModel userModel= userMapper.convertToModel(user);
                String imageBase64 = this.covertImage(user.getImage().getId());
                userModel.setBase64Image(imageBase64);
                userFollowing.add(userModel);
            }
            return userFollowing;
        }

        @Override
        public List<UserModel> followers(int userId) {
            List<userEntity> users = userCustomerRepository.followers(userId);
            List<UserModel> userFollowers = new ArrayList<>();
            for (userEntity user: users){
                UserModel userModel= userMapper.convertToModel(user);
                String imageBase64 = this.covertImage(user.getImage().getId());
                userModel.setBase64Image(imageBase64);
                userFollowers.add(userModel);
            }
            return userFollowers;
        }

    @Override
    public List<UserModel> followingUser(int userId, String userEmail) {
            userEntity userEntity = userRepository.findByEmail(userEmail).get();
            List<userEntity> userFollow = userEntity.getFollowing();
        List<userEntity> users = userCustomerRepository.following(userId);
        List<UserModel> userFollowing = new ArrayList<>();
        for (userEntity user: users){
            UserModel userModel= userMapper.convertToModel(user);
            String imageBase64 = this.covertImage(user.getImage().getId());
            userModel.setBase64Image(imageBase64);
            if(user == userEntity){
                userModel.setStatusFollow("you");
            }
            if(userFollow.contains(user)){
                userModel.setStatusFollow("yes");
            }
            userFollowing.add(userModel);
        }
        return userFollowing;
    }

    @Override
    public List<UserModel> followersUser(int userId, String userEmail) {
        userEntity userEntity = userRepository.findByEmail(userEmail).get();
        List<userEntity> userFollow = userEntity.getFollowing();
        List<userEntity> users = userCustomerRepository.followers(userId);
        List<UserModel> userFollowers = new ArrayList<>();
        for (userEntity user: users){
            UserModel userModel= userMapper.convertToModel(user);
            String imageBase64 = this.covertImage(user.getImage().getId());
            userModel.setBase64Image(imageBase64);
            if(user == userEntity){
                userModel.setStatusFollow("you");
            }
            if(userFollow.contains(user)){
                userModel.setStatusFollow("yes");
            }
            userFollowers.add(userModel);
        }
        return userFollowers;
    }


    @Override
        public void updateInformation(UserProfileModel userProfileModel) {
            userEntity user = userMapper.convertToEntity(userProfileModel);
            userCustomerRepository.updateDetailInformation(user);
        }

        @Override
        public void updateImage(ImageModel image, int userId) {
            imageEntity saveImage = imageMapper.convertToEntity(image);
            imageEntity imageEntity = imageRepository.save(saveImage);
            userCustomerRepository.updateAvatar(imageEntity, userId);

        }

    @Override
    public List<UserModel> suggestFriends(String userEmail) {
            userEntity user = userRepository.findByEmail(userEmail).get();
            List<userEntity> users = userCustomerRepository.following(user.getUserId());
            users.add(user);
            List<userEntity> suggestFriends = userCustomerRepository.suggestFriends(users);
            List<UserModel> userModels = new ArrayList<>();
            for(userEntity user1 : suggestFriends){
                UserModel userModel = new UserModel();
                userModel = userMapper.convertToModel(user1);
                userModels.add(userModel);
            }
        return userModels;
    }

    @Override
    public UserModel getUser(String userEmail) {
        List<UserProfileModel> users = this.SDetailUser(userEmail);
        List<UserModel> userModels = new ArrayList<>();
        for(UserProfileModel user : users){
            UserModel userModel = userMapper.convertPToModel(user);
            userModels.add(userModel);
        }
        return userModels.get(0);
    }

    @Override
    public boolean getUserActive(int userId) {
        List<Token> token = userCustomerRepository.getStatusUser(userId);
        if(!token.isEmpty()){
            return true;
        }
        return false;
    }


}
