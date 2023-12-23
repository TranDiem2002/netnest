package com.webchat.netnest.Controller;

import com.webchat.netnest.Service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping("/countNotice")
    public ResponseEntity<?> countNotice(@AuthenticationPrincipal UserDetails user){
        int countNotice = noticeService.countNotice(user.getUsername());
        return ResponseEntity.ok(countNotice);
    }



}
