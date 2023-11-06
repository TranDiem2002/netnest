package com.netnest.netnest.Service;

import com.netnest.netnest.Model.UserModel;
import org.springframework.stereotype.Service;

public interface UserService {

    UserModel saveUser(UserModel user);
}
