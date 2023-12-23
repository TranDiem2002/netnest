package com.webchat.netnest.Service.Impl;

import com.webchat.netnest.Repository.RepositoryCustomer.Impl.NoticeCustomerRepositoryImpl;
import com.webchat.netnest.Repository.RepositoryCustomer.NoticeCustomerRepository;
import com.webchat.netnest.Repository.UserRepository;
import com.webchat.netnest.Service.NoticeService;
import com.webchat.netnest.entity.userEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoticeServiceImpl implements NoticeService {
    @Autowired
    private NoticeCustomerRepository noticeCustomerRepository;

    @Autowired
    private UserRepository userRepository;

    public NoticeServiceImpl() {
        this.noticeCustomerRepository = new NoticeCustomerRepositoryImpl();
    }

    @Override
    public int countNotice(String userEmail) {
        userEntity user = userRepository.findByEmail(userEmail).get();
        int countNotice = noticeCustomerRepository.countNotice(user.getUserId());
        return countNotice;
    }
}
