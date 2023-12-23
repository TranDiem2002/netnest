package com.webchat.netnest.Repository.RepositoryCustomer.Impl;

import com.webchat.netnest.Repository.CommentRepository;
import com.webchat.netnest.Repository.PostRepository;
import com.webchat.netnest.Repository.RepositoryCustomer.PostCustomerRepository;
import com.webchat.netnest.Repository.UserRepository;
import com.webchat.netnest.entity.commentEntity;
import com.webchat.netnest.entity.imageEntity;
import com.webchat.netnest.entity.postEntity;
import com.webchat.netnest.entity.userEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostCustomerRepositoryImpl implements PostCustomerRepository {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CommentRepository commentRepositry;

    public PostCustomerRepositoryImpl() {

    }

    @Override
    public postEntity updateImagePost(imageEntity image, int postId) {
        postEntity post = postRepository.findById(postId).get();
        List<imageEntity> imageEntities = post.getImage();
        imageEntities.add(image);
        post.setImage(imageEntities);
        return postRepository.save(post);
    }

    @Override
    public postEntity addLike(int postId, int userId) {
        postEntity post = postRepository.findById(postId).get();
        userEntity user = userRepository.findById(userId).get();
        List<userEntity> userEntities = post.getUser();
        userEntities.add(user);
        post.setUser(userEntities);
        return postRepository.save(post);
    }

    @Override
    public int countLikes(int postID) {

        StringBuilder sql = new StringBuilder("select count(user_like) from like_post where post = :postID");
        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("postID", postID);
        Number result = (Number) query.getSingleResult();
        return result.intValue();
    }

    @Override
    public List<userEntity> getUserLikes(int postId) {
        StringBuilder sql = new StringBuilder("select * from user where user_id in (select user_like from like_post where post = :postId)");
        Query query = entityManager.createNativeQuery(sql.toString(),userEntity.class);
        query.setParameter("postId", postId);
        return query.getResultList();
    }

    @Override
    public postEntity addComment(int postId, commentEntity comment) {
        commentEntity saveComment = commentRepositry.save(comment);
        postEntity post = postRepository.findById(postId).get();
        List<commentEntity> commentEntities = post.getCommentEntities();
        commentEntities.add(saveComment);
        post.setCommentEntities(commentEntities);
        return postRepository.save(post);
    }

    @Override
    public int countComment(int postId) {
        StringBuilder sql = new StringBuilder("select count(comment) from comment_post where post = :postId");
        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("postId", postId);
        Number result = (Number) query.getSingleResult();
        return result.intValue();
    }

    @Override
    public List<commentEntity> getComment(int postId) {
        StringBuilder sql = new StringBuilder("select * from comment where commentid in (select comment from comment_post where post = :postId)");
        Query query = entityManager.createNativeQuery(sql.toString(), commentEntity.class);
        query.setParameter("postId", postId);
        return query.getResultList();
    }


}