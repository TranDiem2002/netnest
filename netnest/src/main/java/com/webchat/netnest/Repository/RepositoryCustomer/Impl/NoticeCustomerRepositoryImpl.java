package com.webchat.netnest.Repository.RepositoryCustomer.Impl;

import com.webchat.netnest.Repository.RepositoryCustomer.NoticeCustomerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

@Repository
public class NoticeCustomerRepositoryImpl implements NoticeCustomerRepository {
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public int countNotice(int userId) {
        StringBuilder sql = new StringBuilder("select count(notice) from user_notice where user = :userId");
        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("userId", userId);
        Number result = (Number) query.getSingleResult();
        return result.intValue();
    }
}
