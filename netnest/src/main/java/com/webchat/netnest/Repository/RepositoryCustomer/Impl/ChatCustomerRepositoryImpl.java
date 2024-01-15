package com.webchat.netnest.Repository.RepositoryCustomer.Impl;

import com.webchat.netnest.Repository.ChatRepository;
import com.webchat.netnest.Repository.RepositoryCustomer.ChatCustomerRepository;
import com.webchat.netnest.entity.chatEntity;
import com.webchat.netnest.entity.messageEntity;
import com.webchat.netnest.entity.userEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class ChatCustomerRepositoryImpl implements ChatCustomerRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ChatRepository chatRepository;





    @Override
    public List<Integer> getChatId(int userId) {
        StringBuilder sql = new StringBuilder("select chatid from chat_user where user_chat = :userId");
        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("userId",userId);
        return query.getResultList();
    }


    @Override
    public List<Integer> getChatUser(List<Integer> userId, Integer userMess) {
        StringBuilder sql = new StringBuilder("select chat_user.chatid from chat_user join chat_id ");
        sql.append(" on chat_user.chatid = chat_id.chatid where chat_id.count_user = :j and user_chat = :userMess ");
        for (Integer i : userId){
            sql.append(" and chat_user.chatid  in (select chatid from chat_user WHERE user_chat = "+ i );
        }
        for (int i =0 ; i< userId.size(); i++){
            sql.append(")");
        }
        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("j",userId.size()+1);
        query.setParameter("userMess", userMess);
        return query.getResultList();
    }

    @Override
    public List<messageEntity> getDetailChat(int chatId) {
        StringBuilder sql = new StringBuilder("select * from message where chatid_chatid = :chatId order by create_date desc");
        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("chatId", chatId);
        return query.getResultList();
    }

    @Override
    public List<Integer> getChatModel(int userId) {
        StringBuilder sql = new StringBuilder("select chat_id.chatid from chat_id join chat_user on chat_id.chatid = chat_user.chatid ");
        sql.append(" where user_chat = :userId order by chat_id.date_last_mess desc");
        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("userId", userId);
        return query.getResultList();
    }


    @Override
    public List<messageEntity> getMess(int chatId) {
        StringBuilder sql = new StringBuilder("select message.id, message,create_by, create_date, modified_date,");
        sql.append("  status_mess from message JOIN chat_mess on message.id = chat_mess.mess_id ");
        sql.append("where chat_mess.chatid = :chatId order by create_date desc limit 1");
        Query query = entityManager.createNativeQuery(sql.toString(), messageEntity.class);
        query.setParameter("chatId", chatId);
        return query.getResultList();
    }

    public chatEntity addMessCreate(int chatId, messageEntity messageEntity){
        chatEntity chat = chatRepository.findById(chatId).get();
        List<messageEntity> messageEntities = chat.getMessageEntities();
        messageEntities.add(messageEntity);
        chat.setMessageEntities(messageEntities);
        return chatRepository.save(chat);
    }

    @Override
    public chatEntity addMess( messageEntity message, int chatId) {
        chatEntity chat = chatRepository.findById(chatId).get();
        List<messageEntity> messageEntities = chat.getMessageEntities();
        messageEntities.add(message);
        chat.setMessageEntities(messageEntities);
        chat.setDateLastMess(new Date());
        return chatRepository.save(chat);
    }

    @Override
    public chatEntity checkChat(userEntity user, chatEntity chat,boolean statusChat) {
        List<userEntity> userReader = chat.getUserReaded();
        chatEntity chat1 = new chatEntity();
        if(!statusChat){
            if(!userReader.contains(user)){
                userReader.add(user);
                chat.setUserReaded(userReader);
               chat1 = chatRepository.save(chat);
            }
        }
        else{
            if(userReader.contains(user)){
                userReader.remove(user);
                chat.setUserReaded(userReader);
                chat1 = chatRepository.save(chat);
            }
        }
        return chat1;
    }

}
