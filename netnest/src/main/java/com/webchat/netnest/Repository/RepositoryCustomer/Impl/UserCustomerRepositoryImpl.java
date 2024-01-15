package com.webchat.netnest.Repository.RepositoryCustomer.Impl;

import com.webchat.netnest.Repository.RepositoryCustomer.UserCustomerRepository;
import com.webchat.netnest.Repository.UserRepository;
import com.webchat.netnest.entity.Token;
import com.webchat.netnest.entity.imageEntity;
import com.webchat.netnest.entity.userEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class UserCustomerRepositoryImpl implements UserCustomerRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;
    @Override
    public List<userEntity> findEmailByEmail(String email) {
        StringBuilder sql = new StringBuilder("select * from user where email = '"+email+"'");
        System.out.println(sql);
        Query query = entityManager.createNativeQuery(sql.toString(), userEntity.class);
        return  query.getResultList();
    }

    @Override
    public List<userEntity> findUserByUserName(String username) {
        StringBuilder sql = new StringBuilder("select * from user where user_name = '"+username+"'");
        Query query = entityManager.createNativeQuery(sql.toString(), userEntity.class);
        query.setParameter("username", username);
        return  query.getResultList();
    }

    @Override
    public List<userEntity> findUserByFullName(String fullname) {
        StringBuilder sql = new StringBuilder("select * from user where full_name = '"+fullname+"'");
        Query query = entityManager.createNativeQuery(sql.toString(), userEntity.class);
        query.setParameter("fullname", fullname);
        return  query.getResultList();
    }

    @Override
    public List<userEntity> findUserName(String username) {
        StringBuilder sql = new StringBuilder("select * from user where user_name like '%" + username +"%' or full_name like '%" + username +"%'");
        Query query = entityManager.createNativeQuery(sql.toString(), userEntity.class);
        return  query.getResultList();
    }


    @Override
    public void saveFollowing(userEntity user, userEntity userFollowing) {
        List<userEntity> userF = user.getFollowing();
        userF.add(userFollowing);
        user.setFollowing(userF);
        userRepository.save(user);
    }

    @Override
    public int countFollowing(int userId) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(following_id) FROM following WHERE user_id = :userId");
        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("userId", userId);
        Number result = (Number) query.getSingleResult();
        return result.intValue() ;
    }

    @Override
    public int countFollowers(int userId) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(user_id) FROM following WHERE following_id = :userId");
        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("userId", userId);
        Number result = (Number) query.getSingleResult();
        return result.intValue() ;
    }

    @Override
    public List<userEntity> following(int userId) {
        StringBuilder sql = new StringBuilder("select * from user where user_id in ");
        sql.append("(select following_id from following where user_id = :userId)");
        Query query = entityManager.createNativeQuery(sql.toString(),userEntity.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Override
    public List<userEntity> followers(int userId) {
        StringBuilder sql = new StringBuilder("select * from user where user_id in ");
        sql.append("(select user_id from following where following_id = :userId)");
        Query query = entityManager.createNativeQuery(sql.toString(),userEntity.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Transactional
    @Modifying
    @Override
    public void updateDetailInformation(userEntity user) {
        StringBuilder sql = new StringBuilder("Update user set ");

        if (user.getUserName() != null){
            sql.append("user_name = '" + user.getUserName()+"'");
        }
        if (user.getGender() != null){
            sql.append(" ,gender = '" + user.getGender()+"'");
        }
        if (user.getDateOfBirth() != null){
            sql.append(" ,date_of_birth = '"+ user.getDateOfBirth()+"'");
        }
        if (user.getPhone() != null){
            sql.append(" ,phone = '" + user.getPhone()+ "'");
        }
        sql.append(" where user_id = "+user.getUserId());
        Query query = entityManager.createNativeQuery(sql.toString());
        query.executeUpdate();
    }

    @Override
    public void updateAvatar(imageEntity image, int userId) {
        userEntity user = userRepository.findById(userId).get();
        user.setImage(image);
        userRepository.save(user);
    }

    @Override
    public List<userEntity> suggestFriends(List<userEntity> users) {
        StringBuilder sql = new StringBuilder("select * from user where user_id not in ( ");
        for(userEntity user : users){
            sql.append(user.getUserId() +", ");
        }
        sql.append("0)");
        Query query = entityManager.createNativeQuery(sql.toString(), userEntity.class);
        return query.getResultList();
    }

    @Override
    public List<Token> getStatusUser(int userId) {
        StringBuilder sql = new StringBuilder("select * from token where revoked = 0 and user_id = :userId ");
        Query query = entityManager.createNativeQuery(sql.toString(), Token.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }


}