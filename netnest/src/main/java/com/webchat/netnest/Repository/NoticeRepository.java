package com.webchat.netnest.Repository;

import com.webchat.netnest.entity.noticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<noticeEntity, Integer> {
}
