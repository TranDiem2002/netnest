package com.webchat.netnest.Service.Impl;

import com.webchat.netnest.Model.ChatModel;
import com.webchat.netnest.Model.Request.MessageRequest;
import com.webchat.netnest.Model.Response.ChatStatus;
import com.webchat.netnest.Model.Response.MessageResponse;
import com.webchat.netnest.Model.UserModel;
import com.webchat.netnest.Model.chatUsersModel;
import com.webchat.netnest.Repository.ChatRepository;
import com.webchat.netnest.Repository.MessageRepository;
import com.webchat.netnest.Repository.RepositoryCustomer.ChatCustomerRepository;
import com.webchat.netnest.Repository.RepositoryCustomer.Impl.ChatCustomerRepositoryImpl;
import com.webchat.netnest.Repository.UserRepository;
import com.webchat.netnest.Service.ChatService;
import com.webchat.netnest.Service.UserService;
import com.webchat.netnest.entity.chatEntity;
import com.webchat.netnest.entity.messageEntity;
import com.webchat.netnest.entity.userEntity;
import com.webchat.netnest.util.ChatMapper;
import com.webchat.netnest.util.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatCustomerRepository chatCustomerRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MessageRepository messageRepository;


    private ChatMapper chatMapper;

    private UserMapper userMapper;

    public ChatServiceImpl() {
        this.chatCustomerRepository = new ChatCustomerRepositoryImpl();
        this.userService = new UserServiceImpl();
        this.chatMapper = new ChatMapper();
        this.userMapper = new UserMapper();
    }


    @Override
    public List<chatUsersModel> getChatModel(String userEmail) {
        userEntity user = userRepository.findByEmail(userEmail).get();
        List<Integer> chatID = chatCustomerRepository.getChatModel(user.getUserId());
        List<chatEntity> chatEntities = new ArrayList<>();
       for(Integer id : chatID){
           chatEntity chat = chatRepository.findById(id).get();
           if(!chat.getMessageEntities().isEmpty() && chat.getUserChat().size()>=2){
               chatEntities.add(chat);
           }
       }
       List<chatUsersModel> usersModels = new ArrayList<>();
        boolean status = false;
       if(!chatEntities.isEmpty()){
           for(chatEntity chat : chatEntities){
            List<userEntity> userEntities = chat.getUserChat();
            userEntities.remove(user);
                for (userEntity user1: userEntities){
                    status = false;
                    boolean statusUser = userService.getUserActive(user1.getUserId());
                    if (statusUser){
                        status = statusUser;
                    }
                }
                chatUsersModel chatUsersModel = chatMapper.convertUModel(chat,user,status);
               usersModels.add(chatUsersModel);
           }
       }
        return usersModels;
    }

    @Override
    public List<ChatStatus> getChatStatus(String userEmail, List<Integer> chatId) {
        userEntity user = userRepository.findByEmail(userEmail).get();
        List<ChatStatus> chatStatuses = new ArrayList<>();
        String mess;
//        boolean status = false;
        for (Integer i : chatId){
            ChatStatus chatStatus = new ChatStatus();
            chatEntity chat = chatRepository.findById(i).get();
            messageEntity message = chat.getMessageEntities().get(chat.getMessageEntities().size()-1);
            if(message.getCreateBy() == user){
                mess ="You: "+ message.getMessage();
            }
            else {
                mess = message.getCreateBy().getUserName()+ ": "+message.getMessage();
            }
            chatStatus.setChatID(i);
            chatStatus.setMessage(mess);
            chatStatus.setCreateDateMess(chatMapper.convertTimetoString(message.getCreateDate()));
            List<userEntity> userEntities = chat.getUserChat();
            userEntities.remove(user);
            for (userEntity user1: userEntities){
//                boolean statusUser = userService.getUserActive(user1.getUserId());
//                if (statusUser){
                    chatStatus.setStatusChat(userService.getUserActive(user1.getUserId()));
                System.out.println(userService.getUserActive(user1.getUserId()));
//                }
            }
//            chatStatus.setStatusChat(status);
            chatStatuses.add(chatStatus);
        }
        return chatStatuses;
    }

    @Override
    public ChatModel searchChat(String userEmail, List<UserModel> users) {
        userEntity user = userRepository.findByEmail(userEmail).get();
        List<Integer> userId = new ArrayList<>();
        List<userEntity> userEntities = new ArrayList<>();
        for (UserModel userModel: users){
            userId.add(userModel.getUserId());
        }
        List<Integer> chatId = chatCustomerRepository.getChatUser(userId, user.getUserId());
        if(chatId.isEmpty()){
            for (UserModel userModel: users){
              userEntity user1 = userRepository.findById(userModel.getUserId()).get();
              userEntities.add(user1);
            }
            userEntities.add(user);
            chatEntity chat = new chatEntity();
            chat.setUserChat(userEntities);
            chat.setCreateBy(user);
            chat.setCountUser(userEntities.size());
            if(userEntities.size()>2){
                List<messageEntity> messageEntities = new ArrayList<>();
                messageEntity messageEntity = new messageEntity();
                messageEntity.setMessage(" created the group.");
                messageEntity.setCreateBy(user);
                messageEntity.setCreateDate(new Date());
                messageEntity.setStatusMes(true);
                messageEntities.add(messageEntity);
                chat.setMessageEntities(messageEntities);
            }
            chat.setDateLastMess(new Date());
            chatEntity saveChat = chatRepository.save(chat);
            chatId.add(saveChat.getChatID());
        }
        chatEntity chat = chatRepository.findById(chatId.get(0)).get();
        ChatModel chatModel = chatMapper.convertModel(chat);
        return chatModel;
    }

    @Override
    public List<MessageResponse> addMess(String userEmail, MessageRequest mess) {
        userEntity user = userRepository.findByEmail(userEmail).get();
        int chatId = mess.getChatID();
        messageEntity message = new messageEntity();
        message.setMessage(mess.getMessage());
        message.setCreateBy(user);
        message.setCreateDate(new Date());
       chatEntity  saveChat = chatCustomerRepository.addMess(message, chatId);
       List<messageEntity> messageEntities = saveChat.getMessageEntities();
       List<MessageResponse> messageResponses = chatMapper.convertResponse(messageEntities);
        return messageResponses;
    }

    @Override
    public List<MessageResponse> deleteMess(int chatId, int messId) {
        chatEntity chat = chatRepository.findById(chatId).get();
        messageEntity mess = messageRepository.findById(messId).get();
        List<messageEntity> messageEntities = chat.getMessageEntities();
        if(messageEntities.contains(mess)){
            messageEntities.remove(mess);
            chat.setMessageEntities(messageEntities);
            chatRepository.save(chat);
        }
        List<messageEntity> message = chat.getMessageEntities();
        List<MessageResponse> messageResponses = chatMapper.convertResponse(message);
        return messageResponses;
    }

    @Override
    public ChatModel searchChatById(String userEmail, int chatIdNew,int chatIdOld) {
        userEntity user = userRepository.findByEmail(userEmail).get();
        ChatModel chatModel;
        if(chatIdOld == 0){
            chatEntity chat = chatRepository.findById(chatIdNew).get();
            List<userEntity> userReader= chat.getUserReaded();
            if (!userReader.contains(user)) {
                userReader.add(user);
                chat.setUserReaded(userReader);
                chatRepository.save(chat);
            }
            chatModel = chatMapper.convertModel(chat);
            List<userEntity> chatUser = chat.getUserChat();
            chatUser.remove(user);
            for(userEntity user1: chatUser){
                boolean statusUser = userService.getUserActive(user1.getUserId());
                if(statusUser){
                    chatModel.setStatusChat(statusUser);
                }
            }
        }else{
            chatEntity chat = chatRepository.findById(chatIdOld).get();
            List<userEntity> userReader  = chat.getUserReaded();
            if (!userReader.contains(user)) {
                userReader.remove(user);
                chat.setUserReaded(userReader);
                chatRepository.save(chat);
            }
            chatEntity chatEntity = chatRepository.findById(chatIdNew).get();
            chatModel = chatMapper.convertModel(chatEntity);
            List<userEntity> chatUser = chat.getUserChat();
            chatUser.remove(user);
            for(userEntity user1: chatUser){
                boolean statusUser = userService.getUserActive(user1.getUserId());
                if(statusUser){
                    chatModel.setStatusChat(statusUser);
                }
            }
        }
        return chatModel;
    }

    @Override
    public List<MessageResponse> checkMess(String userEmail, int chatId) {
        userEntity user = userRepository.findByEmail(userEmail).get();
        chatEntity chat = chatRepository.findById(chatId).get();
        List<messageEntity> messageEntities = chat.getMessageEntities();
        List<MessageResponse> messageResponses = chatMapper.convertResponse(messageEntities);
        return messageResponses;
    }

    @Override
    public ChatModel addPeopleChat(String userEmail, int chatId, int userId) {
        userEntity user1 = userRepository.findByEmail(userEmail).get();
        userEntity user = userRepository.findById(userId).get();
        chatEntity chat = chatRepository.findById(chatId).get();
        List<userEntity> userEntities = chat.getUserChat();
        userEntities.add(user);
        chat.setUserChat(userEntities);
        List<messageEntity> mess = chat.getMessageEntities();
        messageEntity message = new messageEntity();
        message.setMessage(user.getUserName() + " added the group.");
        message.setCreateBy(user1);
        message.setCreateDate(new Date());
        mess.add(message);
        chat.setMessageEntities(mess);
        chatEntity chat1 = chatRepository.save(chat);
        ChatModel chatModel = chatMapper.convertModel(chat1);
        return chatModel;
    }

    @Override
    public void leaveChat(String userEmail, int chatId) {
        userEntity user = userRepository.findByEmail(userEmail).get();
        chatEntity chat = chatRepository.findById(chatId).get();
        List<userEntity> userEntities = chat.getUserChat();
        List<messageEntity> mess = chat.getMessageEntities();
        messageEntity message = new messageEntity();
        message.setMessage(user.getUserName() + " left the group.");
        message.setCreateBy(user);
        message.setCreateDate(new Date());
        mess.add(message);
        chat.setMessageEntities(mess);
        userEntities.remove(user);
        chat.setUserChat(userEntities);
        chatRepository.save(chat);
    }

    @Override
    public String changeChatName(String userEmail, int chatId, String chatName) {
        userEntity user = userRepository.findByEmail(userEmail).get();
        chatEntity chat = chatRepository.findById(chatId).get();
        chat.setChatName(chatName);
        List<messageEntity> message = chat.getMessageEntities();
        messageEntity messageEntity = new messageEntity();
        messageEntity.setMessage(user.getUserName() + "named the group " + chatName);
        messageEntity.setCreateBy(user);
        messageEntity.setCreateDate(new Date());
        message.add(messageEntity);
        chat.setMessageEntities(message);
        chatEntity chat1 = chatRepository.save(chat);
        return chat1.getChatName();
    }


}
