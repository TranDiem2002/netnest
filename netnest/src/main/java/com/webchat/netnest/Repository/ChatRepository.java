package com.webchat.netnest.Repository;

import com.webchat.netnest.entity.chatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<chatEntity, Integer> {
}
