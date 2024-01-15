package com.webchat.netnest.Repository.RepositoryCustomer;

import com.webchat.netnest.entity.chatEntity;
import com.webchat.netnest.entity.messageEntity;
import com.webchat.netnest.entity.userEntity;

import java.util.List;

public interface ChatCustomerRepository {

    List<Integer> getChatId(int userId);

    List<Integer> getChatUser( List<Integer> userId, Integer userEmail);

    List<messageEntity> getDetailChat(int chatId);

    List<Integer> getChatModel(int userId);

    List<messageEntity> getMess(int chatId);

    chatEntity addMessCreate(int chatId, messageEntity messageEntity);

    chatEntity addMess( messageEntity message, int chatId);


    chatEntity checkChat(userEntity user, chatEntity chat, boolean statusChat);
}
