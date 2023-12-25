package com.webchat.netnest.Controller;


import com.webchat.netnest.Model.ImageModel;
import com.webchat.netnest.Model.RegisterRequest;
import com.webchat.netnest.Model.Request.AuthenticationRequest;
import com.webchat.netnest.Model.Response.AuthenticationResponse;
import com.webchat.netnest.Model.UserModel;
import com.webchat.netnest.Model.UserProfileModel;
import com.webchat.netnest.Repository.UserRepository;
import com.webchat.netnest.Service.ImageService;
import com.webchat.netnest.Service.UserService;
import com.webchat.netnest.Service.UserServiceDetail;
import com.webchat.netnest.entity.userEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserServiceDetail userServiceDetail;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/register")
    public ResponseEntity<?> register (@RequestBody RegisterRequest request){
        String response = userServiceDetail.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authentication (@RequestBody AuthenticationRequest request){
        AuthenticationResponse response = userServiceDetail.authentication(request);
        userServiceDetail.updateToken(request);
        return ResponseEntity.ok(response);
    }


    @GetMapping(value = "/search/{username}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> searchByUserName(@PathVariable("username") String userName) throws SQLException {
        List<UserModel> userModel = userService.searchUser(userName);
        return ResponseEntity.ok(userModel);
    }


    @GetMapping(value = "/searchDetail/{username}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> searchDetailByUser(@PathVariable("username") String userName) {
        UserProfileModel userModel = userService.searchDetailUser(userName);
        return ResponseEntity.ok(userModel);
    }

    @PostMapping(value = "/users/follow")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> saveFollowing(@AuthenticationPrincipal UserDetails principal, @RequestBody String username)
    {
        userEntity user = userRepository.findByEmail(principal.getUsername()).get();
        userService.saveFollowing(user.getUserId(), username);
        return ResponseEntity.ok("Following thanh cong");
    }

    @GetMapping(value = "/following", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFollowing(@AuthenticationPrincipal UserDetails principal){
        userEntity user = userRepository.findByEmail(principal.getUsername()).get();
        List<UserModel> following = userService.following(user.getUserId());
        return ResponseEntity.ok(following);
    }

    @GetMapping(value = "/followers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFollowers(@AuthenticationPrincipal UserDetails principal){
        userEntity user = userRepository.findByEmail(principal.getUsername()).get();
        List<UserModel> following = userService.followers(user.getUserId());
        return ResponseEntity.ok(following);
    }

    @PutMapping(value = "/user/update")
    public  ResponseEntity<?> updateInformation(@RequestBody UserProfileModel user, @AuthenticationPrincipal UserDetails principal){
        userEntity users = userRepository.findByEmail(principal.getUsername()).get();
        user.setUserId(users.getUserId());
        user.setUserName(users.getUserName());
        userService.updateInformation(user);
        return  ResponseEntity.ok("Update thanh cong");
    }

    @PutMapping(value = "/user/updateAvatar")
    public ResponseEntity<?> updateAvatar(@RequestParam("image") MultipartFile file, @AuthenticationPrincipal UserDetails principal) throws IOException, SQLException {
        byte[] bytes = file.getBytes();
        Blob blob = new SerialBlob(bytes);
        ImageModel imageModel = new ImageModel();
        imageModel.setImage(blob);
        userEntity user = userRepository.findByEmail(principal.getUsername()).get();
        userService.updateImage(imageModel,user.getUserId());
        return ResponseEntity.ok("Update thanh cong");
    }

    @GetMapping("/suggestFriend")
    public ResponseEntity<?> suggestUser(@AuthenticationPrincipal UserDetails user){
        List<UserModel> userModels = userService.suggestFriends(user.getUsername());
        return ResponseEntity.ok(userModels);
    }

}
