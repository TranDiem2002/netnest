package com.webchat.netnest.Controller;

import com.webchat.netnest.Model.*;
import com.webchat.netnest.Model.Request.CommentRequest;
import com.webchat.netnest.Model.Response.CommentResponse;
import com.webchat.netnest.Repository.UserRepository;
import com.webchat.netnest.Service.ImageService;
import com.webchat.netnest.Service.Impl.ImageServiceImpl;
import com.webchat.netnest.Service.Impl.UserServiceImpl;
import com.webchat.netnest.Service.PostService;
import com.webchat.netnest.Service.UserService;
import com.webchat.netnest.entity.userEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

@Controller

//@RequestMapping("/api/user")
public class PostController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private List<PostModel> postModels ;

    public PostController() {
        this.imageService = new ImageServiceImpl();
        this.userService = new UserServiceImpl();
    }

    @GetMapping("/home")
    public ResponseEntity<?> getPost(@AuthenticationPrincipal UserDetails user){
        List<PostModel> postModels1 = postService.getPostHome(user.getUsername());
        return ResponseEntity.ok(postModels1);
    }

    @PostMapping(value = "/{postId}/newImagePost", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createImagePost(@RequestParam("image") MultipartFile file, @PathVariable(name = "postId") int postId) throws IOException, SQLException {
        byte[] bytes = file.getBytes();
        Blob blob = new SerialBlob(bytes);
        ImageModel image = new ImageModel();
        image.setImage(blob);
        PostModel savePost = postService.createImagePost(image, postId);
        return new ResponseEntity<>(savePost, HttpStatus.OK);
    }

    @PostMapping(value = "/{postId}/newVideoPost", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createVideoPost(@RequestParam("video") MultipartFile file, @PathVariable(name = "postId") int postId) throws IOException, SQLException {
        byte[] bytes = file.getBytes();
        Blob blob = new SerialBlob(bytes);
        VideoModel video = new VideoModel();
        video.setVideo(blob);
        PostModel savePost = postService.createVideoPost(video, postId);
        return new ResponseEntity<>(savePost, HttpStatus.OK);
    }

    @PostMapping(value = "/newPost", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createPost(@RequestBody PostModel postModel, @AuthenticationPrincipal UserDetails principal){
        PostModel post = postService.createPost(postModel, principal.getUsername());
        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }



    @GetMapping(value = "/postID")
    public  ResponseEntity<?> getPost(@RequestParam(name = "postId") int postId, @AuthenticationPrincipal UserDetails user){
        PostModel postModel = postService.getPost(postId, user.getUsername());
        return  new ResponseEntity<>(postModel, HttpStatus.OK);
    }

    @PostMapping(value = "/post/addLike", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addLikes(@RequestParam(name = "postId") int postId, @AuthenticationPrincipal UserDetails principal)
    {
        PostModel postModel = postService.addLikes(principal.getUsername(), postId);
        return  new ResponseEntity<>(postModel, HttpStatus.OK);
    }

    @DeleteMapping(value = "/post/dislike", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> disLikes(@RequestParam(name = "postId") int postId, @AuthenticationPrincipal UserDetails principal)
    {
       PostModel postModel = postService.disLikes(principal.getUsername(), postId);
        return  new ResponseEntity<>(postModel, HttpStatus.OK);
    }

    @GetMapping(value = "/post/UserLike", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserLikes(@RequestParam(name = "postId") int postId, @AuthenticationPrincipal UserDetails principal){
        List<UserModel> postModels = postService.getUserLikes(postId);
        return new ResponseEntity<>(postModels, HttpStatus.OK);
    }

    @PostMapping(value = "/post/{postId}/addComments")
    public ResponseEntity<?> addComment(@PathVariable(name = "postId") int postId, @RequestBody CommentRequest commentRequest,
                                        @AuthenticationPrincipal UserDetails principal){
        List<CommentResponse> comment = postService.addComment(postId, commentRequest, principal.getUsername());
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }


    @DeleteMapping("/post/deleteComment")
    public  ResponseEntity<?> deleteComment(@AuthenticationPrincipal userEntity user, @RequestParam int commentId, @RequestParam  int postId){
        List<CommentResponse> commentResponses = postService.deleteComment(postId,commentId, user.getUsername());
        return  ResponseEntity.ok(commentResponses);
    }


    @GetMapping(value = "/post/comment")
    public ResponseEntity<?> getComment(@RequestParam(name = "postId") int postId, @AuthenticationPrincipal UserDetails user){
        List<CommentResponse> commentResponses = postService.getComment(postId, user.getUsername());
        return new ResponseEntity<>(commentResponses, HttpStatus.OK);
    }

    @PostMapping(value = "/post/addLikeComment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addLikesComment(@RequestParam(name = "commentId") int commentId, @AuthenticationPrincipal UserDetails principal)
    {
        CommentResponse commentResponse = postService.addLikeComment(commentId, principal.getUsername());
        return  new ResponseEntity<>(commentResponse, HttpStatus.OK);
    }

    @PostMapping("/post/DeleteLikeComment")
    public ResponseEntity<?> dislikeComment(@AuthenticationPrincipal UserDetails user,@RequestParam int commentId){
        CommentResponse commentResponses = postService.deleteLikeComment(commentId, user.getUsername());
        return ResponseEntity.ok(commentResponses);
    }

    @GetMapping(value = "/postDetail")
    public  ResponseEntity<?> getDetailPost(@RequestParam(name = "postId") int postId, @AuthenticationPrincipal UserDetails user){
        PostDetail postDetail = postService.getDetailPost(postId, user.getUsername());
        return new ResponseEntity<>(postDetail, HttpStatus.OK);
    }

    @GetMapping(value = "/profile/post")
    public ResponseEntity<?> getPostUser(@AuthenticationPrincipal UserDetails user){
        List<PostUserModel> postModel = postService.getPostUser(user.getUsername());
        return ResponseEntity.ok(postModel);
    }

    @GetMapping(value = "/profile/SearchPost")
    public ResponseEntity<?> getPostSearchUser(@RequestParam(name = "userId") int userId, @AuthenticationPrincipal UserDetails user){
        List<PostUserModel> postModel = postService.getPostSearchUser(userId);
        return ResponseEntity.ok(postModel);
    }

    @DeleteMapping(value = "/post")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public  void deletePost(@RequestParam(name = "postId") int postId)
    {
        postService.deletePost(postId);
    }
}

