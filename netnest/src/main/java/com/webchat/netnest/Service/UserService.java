package com.webchat.netnest.Service;

import com.webchat.netnest.Config.JwtService;
import com.webchat.netnest.Model.AuthenticationRequest;
import com.webchat.netnest.Model.AuthenticationResponse;
import com.webchat.netnest.Model.RegisterRequest;
import com.webchat.netnest.Repository.UserRepository;
import com.webchat.netnest.entity.Role;
import com.webchat.netnest.entity.UserEntity;
import com.webchat.netnest.util.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    private UserMapper userMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    public UserService() {
        this.userMapper = new UserMapper();
    }

    public AuthenticationResponse register(RegisterRequest request) {
//        var user = UserEntity.builder()
//                .firstName(request.getFirstName())
//                .lastName(request.getLastName())
//                .email(request.getEmail())
//                .password(passwordEncoder.encode(request.getPassword()))
//                .role(Role.USER)
//                .build();
        UserEntity user = userMapper.convertToEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
        var jwtToken = jwtService.generateToken( user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authentication(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassWord()
                )
        );
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken( user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
