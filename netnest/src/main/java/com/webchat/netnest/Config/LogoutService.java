package com.webchat.netnest.Config;

import com.webchat.netnest.Repository.RepositoryCustomer.TokenCustomerRepository;
import com.webchat.netnest.Repository.StatusRepository;
import com.webchat.netnest.Repository.TokenRepository;
import com.webchat.netnest.Service.UserService;
import com.webchat.netnest.entity.Status;
import com.webchat.netnest.entity.Token;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class LogoutService implements LogoutHandler {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private TokenCustomerRepository tokenCustomerRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private StatusRepository statusRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return;
        }
        jwt = authHeader.substring(7);
//        System.out.println(jwt);
//        var storedToken = tokenRepository.findByToken(jwt).orElse(null);
//        System.out.println(storedToken.getTimeLogin());
        Token token = tokenCustomerRepository.findByToken(jwt);
        LocalDateTime logoutTime = LocalDateTime.now();
        Date date = Date.from(logoutTime.atZone(ZoneId.systemDefault()).toInstant());
        if(token !=null){
            token.setExpired(true);
            token.setRevoked(true);
            token.setLogoutTime(date);
            token.setStatus(Status.inactive);
            tokenRepository.save(token);
            SecurityContextHolder.clearContext();
        }
    }
}
