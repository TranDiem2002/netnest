package com.webchat.netnest.Service.Impl;

import com.webchat.netnest.Repository.RepositoryCustomer.ChatCustomerRepository;
import com.webchat.netnest.Repository.RepositoryCustomer.Impl.ChatCustomerRepositoryImpl;
import com.webchat.netnest.Repository.UserRepository;
import com.webchat.netnest.Service.ChatService;
import com.webchat.netnest.entity.userEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private UserRepository userRepository;

    private ChatCustomerRepository chatCustomerRepository;

    public ChatServiceImpl() {
        this.chatCustomerRepository = new ChatCustomerRepositoryImpl();
    }

    @Override
    public int findChatId(String userEmail, String userName) {
        userEntity user1 = userRepository.findByEmail(userEmail).get();
        userEntity user2 = userRepository.findByUserName(userName).get();
        int chatId = chatCustomerRepository.findChatId(user1.getUserId(), user2.getUserId());
        return chatId;
    }
}
