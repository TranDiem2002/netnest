package com.webchat.netnest.Repository.RepositoryCustomer;

import com.webchat.netnest.entity.Token;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TokenCustomerRepository {

    @PersistenceContext
    private EntityManager entityManager;

   public Token findByUserId(int userId){
       StringBuilder sql = new StringBuilder("select * from token where user_id = :userId");
       Query query = entityManager.createNativeQuery(sql.toString(), Token.class);
       query.setParameter("userId", userId);
       return (Token) query.getResultList().get(0);
   }

    public Token findByToken(String jwt){
        StringBuilder sql = new StringBuilder("select * from token where token = :jwt");
        Query query = entityManager.createNativeQuery(sql.toString(), Token.class);
        query.setParameter("jwt", jwt);
        return (Token) query.getResultList().get(0);
    }

    public List<Token> findUserId(int userId){
        StringBuilder sql = new StringBuilder("select * from token where user_id = :userId order by logout_time desc limit  2");
        Query query = entityManager.createNativeQuery(sql.toString(), Token.class);
        query.setParameter("userId", userId);
        return  query.getResultList();
    }

}
