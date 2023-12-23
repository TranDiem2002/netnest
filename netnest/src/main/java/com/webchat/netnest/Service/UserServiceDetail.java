package com.webchat.netnest.Service;

import com.webchat.netnest.Config.JwtService;
import com.webchat.netnest.Model.RegisterRequest;
import com.webchat.netnest.Model.Request.AuthenticationRequest;
import com.webchat.netnest.Model.Response.AuthenticationResponse;
import com.webchat.netnest.Repository.StatusRepository;
import com.webchat.netnest.Repository.TokenRepository;
import com.webchat.netnest.Repository.UserRepository;
import com.webchat.netnest.entity.*;
import com.webchat.netnest.util.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceDetail {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ImageService imageService;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private TokenRepository tokenRepository;

    private UserMapper userMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    public UserServiceDetail() {
        this.userMapper = new UserMapper();
    }

    public AuthenticationResponse register(RegisterRequest request) {
        userEntity user = userMapper.convertToEntity(request);
        user.setPassWord(passwordEncoder.encode(request.getPassWord()));
        user.setRole(Role.USER);
        user.setImage(imageService.findImageById(1L).get());
        userRepository.save(user);
        status_user status_user = new status_user();
        status_user.setUser(user);
        statusRepository.save(status_user);
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
        status_user status = statusRepository.findByUser(user).get();
        status.setUser(user);
        status.setStatus(Status.activate);
        status.setTimeLogin(request.getDateLogin());
        statusRepository.save(status);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private void saveUserToken (userEntity user, String jwt){
        var token = Token.builder()
                .user(user)
                .token(jwt)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }


    private void revokeAllUserTokens(userEntity user){
        var validUserToken = tokenRepository.findAllValidTokenByUser(user.getUserId());
        if(validUserToken.isEmpty()){
            return;
        }
        validUserToken.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserToken);
    }

}
