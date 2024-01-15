package com.webchat.netnest.Repository;

import com.webchat.netnest.entity.messageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<messageEntity, Integer> {
}
