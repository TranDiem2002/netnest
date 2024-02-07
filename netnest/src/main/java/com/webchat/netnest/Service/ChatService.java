package com.webchat.netnest.Service;

import com.webchat.netnest.Model.ChatModel;
import com.webchat.netnest.Model.Request.MessageRequest;
import com.webchat.netnest.Model.Response.ChatStatus;
import com.webchat.netnest.Model.Response.MessageResponse;
import com.webchat.netnest.Model.UserModel;
import com.webchat.netnest.Model.chatUsersModel;

import java.util.List;

public interface ChatService {

    List<chatUsersModel> getChatModel(String userEmail);

    List<ChatStatus> getChatStatus(String userEmail, List<Integer> chatId);

    ChatModel searchChat(String userMail, List<UserModel> users);

    List<MessageResponse> addMess(String userEmail, MessageRequest mess);
    List<MessageResponse> deleteMess(int chatId, int messId);

    ChatModel searchChatById(String userEmail, int chatIdNew, int chatIdOld);

   List<MessageResponse> checkMess(String userEmail, int chatId);

   ChatModel addPeopleChat(String userEmail, int chatId, int userId);

   void leaveChat(String userEmail, int chatId);

   String changeChatName(String userEmail, int chatId, String chatName);

}
