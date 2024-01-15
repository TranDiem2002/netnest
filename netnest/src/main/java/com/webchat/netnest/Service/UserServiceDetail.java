package com.webchat.netnest.Service;

import com.webchat.netnest.Config.JwtService;
import com.webchat.netnest.Model.RegisterRequest;
import com.webchat.netnest.Model.Request.AuthenticationRequest;
import com.webchat.netnest.Model.Response.AuthenticationResponse;
import com.webchat.netnest.Repository.RepositoryCustomer.Impl.UserCustomerRepositoryImpl;
import com.webchat.netnest.Repository.RepositoryCustomer.TokenCustomerRepository;
import com.webchat.netnest.Repository.RepositoryCustomer.UserCustomerRepository;
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

import java.util.Date;

@Service
public class UserServiceDetail {

    @Autowired
    private UserRepository userRepository;


    private UserCustomerRepository userCustomerRepository;

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

    @Autowired
    private TokenCustomerRepository tokenCustomerRepository;

    private UserMapper userMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    public UserServiceDetail() {
        this.userMapper = new UserMapper();
        this.userCustomerRepository = new UserCustomerRepositoryImpl();
    }

    public String register(RegisterRequest request) {
        userEntity user = userMapper.convertToEntity(request);
        if(!userRepository.findByEmail(request.getEmail()).isEmpty()){
            return "email đã tồn tại";
        }
        if(!userRepository.findByUserName(request.getUserName()).isEmpty()){
            return "user_name đã tồn tại";
        }if(!userRepository.findByFullName(request.getFullName()).isEmpty()){
            return "full_name đã tồn tại";
        }



        user.setPassWord(passwordEncoder.encode(request.getPassWord()));
        user.setRole(Role.USER);
        user.setImage(imageService.findImageById(1L).get());
        userRepository.save(user);
        var jwtToken = jwtService.generateToken( user);
        return "đã add user thành công";
//        return AuthenticationResponse.builder()
//                .token(jwtToken)
//                .build();
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
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }



    private void saveUserToken (userEntity user, String jwt){
        Date date = new Date();

        var token = Token.builder()
                .user(user)
                .token(jwt)
                .tokenType(TokenType.BEARER)
                .status(Status.activate)
                .timeLogin(date)
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
