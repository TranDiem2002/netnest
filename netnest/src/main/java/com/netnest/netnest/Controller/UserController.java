package com.netnest.netnest.Controller;

import com.netnest.netnest.Model.UserModel;
import com.netnest.netnest.Service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(name = "/users")
    public UserModel saveUser(@RequestBody UserModel userModel){
        return userService.saveUser(userModel);
    }
}

