package com.webchat.netnest.Repository.RepositoryCustomer.Impl;

import com.webchat.netnest.Repository.RepositoryCustomer.ChatCustomerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

@Repository
public class ChatCustomerRepositoryImpl implements ChatCustomerRepository {

    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public int findChatId(int userId1, int userId2) {
        StringBuilder sql = new StringBuilder("select chatid from chat_id where (user1_user_id = " + userId1 );
        sql.append(" and user2_user_id = " +userId2 + ") OR (user1_user_id = " + userId2 + " and user2_user_id = " +userId1 );
        Query query = entityManager.createNativeQuery(sql.toString());
        Number result = (Number) query.getSingleResult();
        return result.intValue();
    }
}
