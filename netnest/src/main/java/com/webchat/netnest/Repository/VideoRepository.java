package com.webchat.netnest.Repository;

import com.webchat.netnest.entity.videoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<videoEntity, Integer> {
}
