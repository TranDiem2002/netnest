package com.webchat.netnest.Controller;

import com.webchat.netnest.Model.ChatModel;
import com.webchat.netnest.Model.Request.MessageRequest;
import com.webchat.netnest.Model.Response.ChatStatus;
import com.webchat.netnest.Model.Response.MessageResponse;
import com.webchat.netnest.Model.UserModel;
import com.webchat.netnest.Model.chatUsersModel;
import com.webchat.netnest.Service.ChatService;
import com.webchat.netnest.Service.Impl.ChatServiceImpl;
import com.webchat.netnest.Service.Impl.UserServiceImpl;
import com.webchat.netnest.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ChatController {

        @Autowired
        private ChatService chatService;

        @Autowired
        private UserService userService;

    public ChatController() {
        this.chatService =new ChatServiceImpl();
        this.userService = new UserServiceImpl();
    }

    @GetMapping("/chat/user")
    public ResponseEntity<?> getUserChat(@AuthenticationPrincipal UserDetails user){
        return ResponseEntity.ok(userService.getUser(user.getUsername()));
    }

    @GetMapping("/chat/all")
    public ResponseEntity<?> getChat(@AuthenticationPrincipal UserDetails user){
        List<chatUsersModel> chat = chatService.getChatModel(user.getUsername());
        return ResponseEntity.ok(chat);
    }


    @GetMapping("/chat/search")
    public ResponseEntity<?> searchChat(@AuthenticationPrincipal UserDetails user, @RequestBody List<UserModel> users){
        ChatModel chatModels = chatService.searchChat(user.getUsername(), users);
        return  ResponseEntity.ok(chatModels);
    }


    @GetMapping("/chat/searchById")
    public ResponseEntity<?> searchChatById(@AuthenticationPrincipal UserDetails user, @RequestParam int chatIdNew, @RequestParam int chatIdOld){
        ChatModel chatModel = chatService.searchChatById(user.getUsername(),chatIdNew, chatIdOld);
        return  ResponseEntity.ok(chatModel);
    }

    @GetMapping("/chat/checkMess")
    public ResponseEntity<?> checkMess(@AuthenticationPrincipal UserDetails user, @RequestParam int chatId){
        List<MessageResponse> messageResponses = chatService.checkMess(user.getUsername(), chatId);
        return ResponseEntity.ok(messageResponses);
    }

    @PostMapping("/chat/addMess")
    public  ResponseEntity<?> addMess(@AuthenticationPrincipal UserDetails user, @RequestBody MessageRequest messageRequest){
        List<MessageResponse> messageResponses = chatService.addMess(user.getUsername(), messageRequest);

    return ResponseEntity.ok(messageResponses);
    }

    @DeleteMapping("/chat/deleteMess")
    public  ResponseEntity<?> deleteMess(@AuthenticationPrincipal UserDetails user, @RequestParam int chatId, @RequestParam int messId){
        List<MessageResponse> messageResponses = chatService.deleteMess(chatId, messId);

        return ResponseEntity.ok(messageResponses);
    }

    @GetMapping("/chat/checkChatStatus")
    public ResponseEntity<?>checkChatStatus(@AuthenticationPrincipal UserDetails user, @RequestBody List<Integer> chatId){
        List<ChatStatus> chatStatuses = chatService.getChatStatus(user.getUsername(), chatId);
        return ResponseEntity.ok(chatStatuses);
    }

    @PostMapping("/chat/addUser")
    public ResponseEntity<?>addPeopleChat(@AuthenticationPrincipal UserDetails user, @RequestParam int chatId, @RequestParam int userId){
        ChatModel chatModel = chatService.addPeopleChat(user.getUsername(), chatId, userId);
        return ResponseEntity.ok(chatModel);
    }

    @DeleteMapping("/chat/leaveChat")
    public ResponseEntity<?> leaveChat(@AuthenticationPrincipal UserDetails user, @RequestParam int chatId){
        chatService.leaveChat(user.getUsername(), chatId);
        List<chatUsersModel> chat = chatService.getChatModel(user.getUsername());
        return ResponseEntity.ok(chat);
    }
}
