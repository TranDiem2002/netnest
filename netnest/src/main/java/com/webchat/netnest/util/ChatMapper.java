package com.webchat.netnest.util;

import com.webchat.netnest.Model.ChatModel;
import com.webchat.netnest.Model.Response.MessageResponse;
import com.webchat.netnest.Model.UserModel;
import com.webchat.netnest.Model.chatUsersModel;
import com.webchat.netnest.Service.Impl.UserServiceImpl;
import com.webchat.netnest.Service.UserService;
import com.webchat.netnest.entity.chatEntity;
import com.webchat.netnest.entity.messageEntity;
import com.webchat.netnest.entity.userEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatMapper {

    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    public ChatMapper() {
        this.userMapper = new UserMapper();
        this.userService = new UserServiceImpl();
    }

    public String convertTimetoString(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = dateFormat.format(date);
        return formattedDate;
    }


    public MessageResponse convertMessReponse(messageEntity mess){
        MessageResponse messageResponse = modelMapper.map(mess, MessageResponse.class);
        messageResponse.setCreateById(mess.getCreateBy().getUserId());
        messageResponse.setCreateByUserName(mess.getCreateBy().getUserName());
        messageResponse.setCreateDate(convertTimetoString(mess.getCreateDate()));
        return messageResponse;
    }


    public List<MessageResponse> convertResponse (List<messageEntity> mess){
        List<MessageResponse> messageResponses = new ArrayList<>();
        for(messageEntity messageEntity : mess){
            MessageResponse messageResponse = modelMapper.map(messageEntity, MessageResponse.class);
            messageResponse.setCreateById(messageEntity.getCreateBy().getUserId());
            messageResponse.setCreateByUserName(messageEntity.getCreateBy().getUserName());
            messageResponse.setCreateDate(convertTimetoString(messageEntity.getCreateDate()));
            messageResponses.add(messageResponse);
        }
        return  messageResponses;
    }

    public ChatModel convertModel(chatEntity chat){
        ChatModel chatModel = new ChatModel();
        chatModel.setChatID(chat.getChatID());
        List<UserModel> userModels = new ArrayList<>();
        for(userEntity user: chat.getUserChat()){
            UserModel userModel = userMapper.convertToModel(user);
            userModels.add(userModel);
        }
        chatModel.setUserOther(userModels);
        if(!chat.getMessageEntities().isEmpty()){
            chatModel.setMessage(convertResponse(chat.getMessageEntities()));
        }
        chatModel.setChatName(chat.getChatName());
        return  chatModel;
    }



    public chatUsersModel convertUModel(chatEntity chat, userEntity userEntity, boolean statusChat){
        chatUsersModel chatUsersModel = new chatUsersModel();
            if(!chat.getMessageEntities().isEmpty()){
            chatUsersModel.setChatID(chat.getChatID());
            List<userEntity> userEntities = chat.getUserChat();
            userEntities.remove(userEntity);
            List<UserModel> userModels = new ArrayList<>();
            for(userEntity user: userEntities){
                UserModel userModel = userMapper.convertToModel(user);
                userModels.add(userModel);
            }
                messageEntity mess = chat.getMessageEntities().get(chat.getMessageEntities().size()-1);
                chatUsersModel.setCreateDateMess(convertTimetoString(mess.getCreateDate()));
                chatUsersModel.setMessage(mess.getMessage());
                chatUsersModel.setUserOther(userModels);
                chatUsersModel.setChatName(chat.getChatName());
               chatUsersModel.setStatusChat(statusChat);
            }
        return chatUsersModel;
    }


}
