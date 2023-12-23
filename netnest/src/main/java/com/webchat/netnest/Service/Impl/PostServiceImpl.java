package com.webchat.netnest.Service.Impl;

import com.webchat.netnest.Model.ImageModel;
import com.webchat.netnest.Model.PostDetail;
import com.webchat.netnest.Model.PostModel;
import com.webchat.netnest.Model.Request.CommentRequest;
import com.webchat.netnest.Model.Response.CommentResponse;
import com.webchat.netnest.Model.UserModel;
import com.webchat.netnest.Repository.*;
import com.webchat.netnest.Repository.RepositoryCustomer.Impl.PostCustomerRepositoryImpl;
import com.webchat.netnest.Repository.RepositoryCustomer.Impl.UserCustomerRepositoryImpl;
import com.webchat.netnest.Repository.RepositoryCustomer.PostCustomerRepository;
import com.webchat.netnest.Repository.RepositoryCustomer.UserCustomerRepository;
import com.webchat.netnest.Service.PostService;
import com.webchat.netnest.entity.*;
import com.webchat.netnest.util.CommentMapper;
import com.webchat.netnest.util.ImageMapper;
import com.webchat.netnest.util.PostMapper;
import com.webchat.netnest.util.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostCustomerRepository postCustomerRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserCustomerRepository userCustomerRepository;

    @Autowired
    private NoticeRepository noticeRepository;

    private UserMapper userMapper;

    private PostMapper postMapper;

    private ImageMapper imageMapper;

    private CommentMapper commentMapper;

    public PostServiceImpl() {
        this.postMapper = new PostMapper();
        this.imageMapper = new ImageMapper();
        this.userMapper = new UserMapper();
        this.commentMapper = new CommentMapper();
        this.postCustomerRepository = new PostCustomerRepositoryImpl();
        this.userCustomerRepository = new UserCustomerRepositoryImpl();
    }

    @Override
    public PostModel createImagePost(ImageModel imageModel, int postId) {
        imageEntity imageEntity = imageMapper.convertToEntity(imageModel);
        imageEntity saveImage =  imageRepository.save(imageEntity);
        postEntity post = postCustomerRepository.updateImagePost(saveImage,postId);
        return  postMapper.convertToModel(post);
    }

    @Override
    public PostModel createPost(PostModel postModel, String userEmail) {
        postEntity post = postMapper.convertToEntity(postModel);
        userEntity user = userRepository.findByEmail(userEmail).get();
        post.setCreateBy(user);
        postEntity savePost = postRepository.save(post);
        List<userEntity> followers = userCustomerRepository.followers(user.getUserId());
        noticeEntity notice = new noticeEntity();
        notice.setUserEntities(followers);
        notice.setContentNotice(user.getUserName()+" đã thêm một bài viết mới");
        notice.setCreateNotice(savePost.getCreateDate());
        noticeRepository.save(notice);
        return postMapper.convertToModel(savePost);
    }

    public PostModel countLike_Comment(int postId, PostModel postModel){
        int countLikes = postCustomerRepository.countLikes(postId);
        int countComment = postCustomerRepository.countComment(postId);
        postModel.setCountLike(countLikes);
        postModel.setCountComments(countComment);
        return postModel;
    }

    @Override
    public PostModel getPost(int postId) {
        postEntity post = postRepository.findById(postId).get();
        PostModel postModel = postMapper.convertToModel(post);
        return this.countLike_Comment(postId, postModel);
    }

    @Override
    public PostModel addLikes(String userEmail, int postId) {
        userEntity user = userRepository.findByEmail(userEmail).get();
        postEntity post = postCustomerRepository.addLike(postId, user.getUserId());
        PostModel  postModel = postMapper.convertToModel(post);
        return this.countLike_Comment(postId,postModel);
    }

    @Override
    public List<UserModel> getUserLikes(int postId) {
        List<userEntity> userEntities = postCustomerRepository.getUserLikes(postId);
        List<UserModel> userModels = new ArrayList<>();
        for(userEntity user: userEntities){
            UserModel userModel = new UserModel();
            userModel = userMapper.convertToModel(user);
            userModels.add(userModel);
        }
        return userModels;
    }


    @Override
    public PostModel addComment(int postId, CommentRequest commentRequest, String userEmail) {
        userEntity user = userRepository.findByEmail(userEmail).get();
        commentEntity comment1 = commentMapper.convertToEntity(commentRequest);
        comment1.setUser(user);
        commentEntity savecomment = commentRepository.save(comment1);
        postEntity postEntity = postCustomerRepository.addComment(postId,savecomment);
        PostModel postModel = postMapper.convertToModel(postEntity);
        return this.countLike_Comment(postId, postModel);
    }

    @Override
    public List<CommentResponse> getComment(int postId) {
        List<commentEntity> commentEntities = postCustomerRepository.getComment(postId);
        List<CommentResponse> commentResponses = new ArrayList<>();
        for(commentEntity comment : commentEntities){
            CommentResponse commentResponse = new CommentResponse();
            commentResponse = commentMapper.convertToModel(comment);
            commentResponses.add(commentResponse);
        }
        return commentResponses;
    }

    @Override
    public PostDetail getDetailPost(int postId) {
        postEntity post = postRepository.findById(postId).get();
        int countLikes = postCustomerRepository.countLikes(postId);
        PostDetail postDetail = postMapper.convertToDetail(post, countLikes);
        return postDetail;
    }

    @Override
    public void deletePost(int postId) {
        postRepository.deleteById(postId);
    }


}
