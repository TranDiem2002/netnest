package com.webchat.netnest.Service.Impl;

import com.webchat.netnest.Model.*;
import com.webchat.netnest.Model.Request.CommentRequest;
import com.webchat.netnest.Model.Response.CommentResponse;
import com.webchat.netnest.Repository.*;
import com.webchat.netnest.Repository.RepositoryCustomer.Impl.PostCustomerRepositoryImpl;
import com.webchat.netnest.Repository.RepositoryCustomer.Impl.UserCustomerRepositoryImpl;
import com.webchat.netnest.Repository.RepositoryCustomer.PostCustomerRepository;
import com.webchat.netnest.Repository.RepositoryCustomer.TokenCustomerRepository;
import com.webchat.netnest.Repository.RepositoryCustomer.UserCustomerRepository;
import com.webchat.netnest.Service.PostService;
import com.webchat.netnest.entity.*;
import com.webchat.netnest.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private VideoRepository videoRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserCustomerRepository userCustomerRepository;

    @Autowired
    private NoticeRepository noticeRepository;


    @Autowired
    private TokenCustomerRepository tokenCustomerRepository;

    private VideoMapper videoMapper;

    private UserMapper userMapper;

    private PostMapper postMapper;

    private ImageMapper imageMapper;

    private CommentMapper commentMapper;

    public PostServiceImpl() {
        this.postMapper = new PostMapper();
        this.imageMapper = new ImageMapper();
        this.userMapper = new UserMapper();
        this.commentMapper = new CommentMapper();
        this.videoMapper = new VideoMapper();
        this.postCustomerRepository = new PostCustomerRepositoryImpl();
        this.userCustomerRepository = new UserCustomerRepositoryImpl();
    }

    @Override
    public List<PostModel> getPostHome(String userEmail) {
        userEntity user = userRepository.findByEmail(userEmail).get();
        List<Integer> pots = postCustomerRepository.getPostHome(user.getUserId());
        List<Integer> postId = postCustomerRepository.suggestPost(pots, user.getUserId());
        for(Integer i :postId){
            pots.add(i);
        }
        List<PostModel> postModels =new ArrayList<>();
        for(int i = 0; i < pots.size(); i++){
            PostModel postModel = new PostModel();
            postEntity postEntity = postRepository.findById(pots.get(i)).get();
            List<userEntity> userLikes = postEntity.getUser();
            if(!userLikes.contains(user)){
                postModel = postMapper.convertToModel(postEntity);
                postModel.setLikeStatus(userLikes.contains(user));
                postModel.setFollowStatus(getCheckFollowing(user.getFollowing(),postEntity.getCreateBy()));
                PostModel postModel1 = this.countLike_Comment(postEntity.getPostID(), postModel);
                postModels.add(postModel1);
            }
        }
        return postModels;
    }

    @Override
    public PostModel createImagePost(ImageModel imageModel, int postId) {
        imageEntity imageEntity = imageMapper.convertToEntity(imageModel);
        imageEntity saveImage =  imageRepository.save(imageEntity);
        postEntity post = postCustomerRepository.updateImagePost(saveImage,postId);
        return  postMapper.convertToModel(post);
    }

    @Override
    public PostModel createVideoPost(VideoModel video, int postId) {
        videoEntity videoEntity = videoMapper.convertToEntity(video);
        videoEntity saveVideo = videoRepository.save(videoEntity);
        postEntity post = postCustomerRepository.updateVideoPost(saveVideo, postId);

        return postMapper.convertToModel(post);
    }

    @Override
    public PostModel createPost(PostModel postModel, String userEmail) {
        postEntity post = postMapper.convertToEntity(postModel);
       Date date = new Date();
       post.setCreateDate(date);
        userEntity user = userRepository.findByEmail(userEmail).get();
        post.setCreateBy(user);
        postEntity savePost = postRepository.save(post);
        List<userEntity> followers = userCustomerRepository.followers(user.getUserId());
        noticeEntity notice = new noticeEntity();
        notice.setUserEntities(followers);
        notice.setContentNotice(user.getUserName()+" đã thêm một bài viết mới");
        notice.setCreateNotice(savePost.getCreateDate());
        noticeRepository.save(notice);
        return postMapper.convertSavePost(savePost);
    }

    public PostModel countLike_Comment(int postId, PostModel postModel){
        int countLikes = postCustomerRepository.countLikes(postId);
        int countComment = postCustomerRepository.countComment(postId);
        postModel.setCountLike(countLikes);
        postModel.setCountComments(countComment);
        return postModel;
    }

    public PostUserModel countLike_Comment(int postId, PostUserModel postModel){
        int countLikes = postCustomerRepository.countLikes(postId);
        int countComment = postCustomerRepository.countComment(postId);
        postModel.setCountLike(countLikes);
        postModel.setCountComments(countComment);
        return postModel;
    }

    public String convertTimetoString(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String formattedDate = dateFormat.format(date);
        return formattedDate;
    }

    @Override
    public PostModel getPost(int postId, String userEmail) {
        postEntity post = postRepository.findById(postId).get();
        PostModel postModel = postMapper.convertToModel(post);
        postModel.setCreateDate(convertTimetoString(post.getCreateDate()));
        userEntity user = userRepository.findByEmail(userEmail).get();
        List<userEntity> userLikes = post.getUser();
        PostModel postModel1 = this.countLike_Comment(postId, postModel);
        postModel1.setLikeStatus(getCheckLike(userLikes, user));
        postModel1.setFollowStatus(getCheckFollowing(user.getFollowing(),post.getCreateBy()));
        return postModel1;
    }

    @Override
    public PostModel addLikes(String userEmail, int postId) {
        userEntity user = userRepository.findByEmail(userEmail).get();
        postEntity post = postCustomerRepository.addLike(postId, user.getUserId());
        List<userEntity> userLikes = post.getUser();
        PostModel  postModel = postMapper.convertToModel(post);
        postModel.setLikeStatus(userLikes.contains(user));
        noticeEntity notice = new noticeEntity();
        List<userEntity> userEntities = new ArrayList<>();
        userEntities.add(post.getCreateBy());
        notice.setUserEntities(userEntities);
        notice.setContentNotice(user.getUserName()+"đã thích bài viết của bạn");
        notice.setCreateNotice(new Date());
        noticeRepository.save(notice);
        return this.countLike_Comment(postId,postModel);
    }

    @Override
    public PostModel disLikes(String userEmail, int postId) {
        userEntity user = userRepository.findByEmail(userEmail).get();
        postEntity post = postCustomerRepository.disLike(postId, user.getUserId());
        PostModel postModel = postMapper.convertToModel(post);
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
    public List<CommentResponse> addComment(int postId, CommentRequest commentRequest, String userEmail) {
        userEntity user = userRepository.findByEmail(userEmail).get();
        commentEntity comment1 = commentMapper.convertToEntity(commentRequest);
        comment1.setUser(user);
        comment1.setUserLikes(new ArrayList<>());
        comment1.setCreateDate(new Date());
        commentEntity savecomment = commentRepository.save(comment1);
        postEntity postEntity = postCustomerRepository.addComment(postId,savecomment);
        List<commentEntity> commentEntities = postCustomerRepository.getComment(postId);
        List<CommentResponse> commentResponses = new ArrayList<>();
        for(commentEntity comment : commentEntities){
            List<userEntity> userLikes = comment.getUserLikes();
            CommentResponse commentResponse = new CommentResponse();
            commentResponse = commentMapper.convertToModel(comment);
            commentResponse.setCountLike(postCustomerRepository.countLikesComment(comment.getCommentID()));
            if(userLikes.contains(user)){
                commentResponse.setStatusLike(true);
            }
            commentResponses.add(commentResponse);
        }

//        noticeEntity notice = new noticeEntity();
//        List<userEntity> userEntities = new ArrayList<>();
//        userEntities.add(postEntity.getCreateBy());
//        notice.setUserEntities(userEntities);
//        notice.setContentNotice(user.getUserName()+"đã bình luận bài viết của bạn");
//        notice.setCreateNotice(new Date());
//        noticeRepository.save(notice);
        return commentResponses;
    }

    @Override
    public List<CommentResponse> deleteComment(int postId, int commentId, String userEmail) {
        postEntity post = postRepository.findById(postId).get();
        List<commentEntity> commentEntities = post.getCommentEntities();
        commentEntity comment  = commentRepository.findById(commentId).get();
        commentEntities.remove(comment);
        post.setCommentEntities(commentEntities);
        postEntity postEntity= postRepository.save(post);
        List<commentEntity> comments = postEntity.getCommentEntities();
        List<CommentResponse> commentResponses = new ArrayList<>();
        for(commentEntity comm: comments){
            CommentResponse commentResponse = commentMapper.convertToModel(comm);
            commentResponses.add(commentResponse);
        }
        return commentResponses;
    }


    @Override
    public CommentResponse deleteLikeComment(int commentId, String userEmail) {
        userEntity user = userRepository.findByEmail(userEmail).get();
        commentEntity comment = commentRepository.findById(commentId).get();
        List<userEntity> userLikes = comment.getUserLikes();
        if(userLikes.contains(user)){
            userLikes.remove(user);
            comment.setUserLikes(userLikes);
        }
        commentEntity saveComment = commentRepository.save(comment);
        CommentResponse commentResponse = commentMapper.convertToModel(saveComment);
        int countLike = postCustomerRepository.countLikesComment(commentId);
        commentResponse.setCountLike(countLike);
        commentResponse.setStatusLike(true);
        return commentResponse;
    }

    @Override
    public List<CommentResponse> getComment(int postId, String userEmail) {
        userEntity user = userRepository.findByEmail(userEmail).get();
        List<commentEntity> commentEntities = postCustomerRepository.getComment(postId);
        List<CommentResponse> commentResponses = new ArrayList<>();
        for(commentEntity comment : commentEntities){
            List<userEntity> userLikes = comment.getUserLikes();
            CommentResponse commentResponse = new CommentResponse();
            commentResponse = commentMapper.convertToModel(comment);
            commentResponse.setCountLike(postCustomerRepository.countLikesComment(comment.getCommentID()));
            if(userLikes.contains(user)){
                commentResponse.setStatusLike(true);
            }
            commentResponses.add(commentResponse);
        }
        return commentResponses;
    }

    @Override
    public CommentResponse addLikeComment(int commentId, String UserEmail) {
        commentEntity comment = commentRepository.findById(commentId).get();
        userEntity user = userRepository.findByEmail(UserEmail).get();
        List<userEntity>userLike = comment.getUserLikes();
        userLike.add(user);
        commentEntity saveComment = commentRepository.save(comment);
        CommentResponse commentResponse = commentMapper.convertToModel(saveComment);
        int countLike = postCustomerRepository.countLikesComment(commentId);
        commentResponse.setCountLike(countLike);
        commentResponse.setStatusLike(true);
        return commentResponse;
    }

    public boolean getCheckLike(List<userEntity> userLike, userEntity user){
        return userLike.contains(user);
    }

    public boolean getCheckFollowing(List<userEntity> following, userEntity user){return following.contains(user);}

    @Override
    public PostDetail getDetailPost(int postId, String userEmail) {
        postEntity post = postRepository.findById(postId).get();
        int countLikes = postCustomerRepository.countLikes(postId);
        PostDetail postDetail = postMapper.convertToDetail(post, countLikes);
        List<CommentResponse> commentResponses = this.getComment(postId,userEmail );
        postDetail.setComments(commentResponses);
        userEntity user = userRepository.findByEmail(userEmail).get();
        postDetail.setLikeStatus(getCheckLike(post.getUser(),user));
        postDetail.setFollowStatus(getCheckFollowing(user.getFollowing(),post.getCreateBy()));
        return postDetail;
    }

    @Override
    public List<PostUserModel> getPostUser(String userEmail) {
        userEntity user = userRepository.findByEmail(userEmail).get();
        List<Integer>  postIds = postCustomerRepository.getPostProfile(user.getUserId());
        List<PostUserModel> postModels = new ArrayList<>();
        for(Integer i : postIds){
            postEntity post = postRepository.findById(i).get();
            if(!post.getImage().isEmpty()|| !post.getVideo().isEmpty()){
                PostUserModel postModel = postMapper.convertToModelProfile(post);
                PostUserModel postModel1 =  countLike_Comment(i,postModel);
                postModels.add(postModel);
            }
        }
        return postModels;
    }

    @Override
    public List<PostUserModel> getPostSearchUser(int userId) {
        List<Integer>  postIds = postCustomerRepository.getPostProfile(userId);
        List<PostUserModel> postModels = new ArrayList<>();
        for(Integer i : postIds){
            postEntity post = postRepository.findById(i).get();
            PostUserModel postModel = postMapper.convertToModelProfile(post);
            PostUserModel postModel1 =  countLike_Comment(i,postModel);
            postModels.add(postModel);
        }
        return postModels;
    }

    @Override
    public void deletePost(int postId) {
        postRepository.deleteById(postId);
    }


}
