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
import com.webchat.netnest.entity.Status;
import com.webchat.netnest.entity.imageEntity;
import com.webchat.netnest.entity.status_user;
import com.webchat.netnest.entity.userEntity;
import com.webchat.netnest.util.ImageMapper;
import com.webchat.netnest.util.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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


//        @Override
//        public UserProfileModel createUser(UserProfileModel userModel) {
//            List<userEntity> users = userCustomerRepository.findEmailByEmail(userModel.getEmail());
//            if(users.size() > 0){
//                userEntity user1 = new userEntity();
//                user1.setEmail(users.get(0).getEmail());
//                return userMapper.convertToProfileModel(user1);
//            }
//
//            List<userEntity> users1 = userCustomerRepository.findUserByUserName(userModel.getUserName());
//            if(users1.size() > 0){
//                userEntity user1 = new userEntity();
//                user1.setUserName(users1.get(0).getUsername());
//                return userMapper.convertToProfileModel(user1);
//            }
//
//            userEntity user = userMapper.convertToEntity(userModel);
//            user.setImage(imageService.findImageById(1L).get());
//            userEntity saveUser = userRepository.save(user);
//            return userMapper.convertToProfileModel(saveUser);
//        }

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

        public List<UserProfileModel> searchPUser(String username)  {
            List<userEntity> user1 = userCustomerRepository.findUserName(username);
            List<UserProfileModel> userP = new ArrayList<>();
            for (userEntity user : user1) {
                System.out.println(user.getUserName());
                String base64Image = this.covertImage(user.getImage().getId());
                UserProfileModel userProfileModel = userMapper.convertToProfileModel(user, base64Image);
                userP.add(userProfileModel);
            } return userP;
        }

        @Override
        public List<UserModel> searchUser(String username)  {
            List<UserProfileModel> users = this.searchPUser(username);
            List<UserModel> userModels = new ArrayList<>();
            for(UserProfileModel user : users){
                UserModel userModel = userMapper.convertPToModel(user);
                userModels.add(userModel);
            }
            return userModels;
        }

        @Override
        public UserProfileModel searchDetailUser(String username) {
            List<UserProfileModel> users = this.searchPUser(username);
            UserProfileModel userP = users.get(0);
            int countFollowing = userCustomerRepository.countFollowing(userP.getUserId());
            int countFollowers = userCustomerRepository.countFollowers(userP.getUserId());
            userP.setCountfollowing(countFollowing);
            userP.setCountfollowers(countFollowers);
            return userP;
        }

        @Override
        public void saveFollowing(int userId, String userName) {
            List<userEntity> user1 = userCustomerRepository.findUserName(userName);
            Optional<userEntity> userEntity = userRepository.findById(userId);
            for (userEntity user : user1){
                userCustomerRepository.saveFollowing(userEntity.get(), user);
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
    public void logout(Date date, String userEmail) {
            userEntity user = userRepository.findByEmail(userEmail).get();
        status_user status_user = statusRepository.findByUser(user).get();
        status_user.setTimeLogout(date);
        status_user.setStatus(Status.inactive);
    }


}
