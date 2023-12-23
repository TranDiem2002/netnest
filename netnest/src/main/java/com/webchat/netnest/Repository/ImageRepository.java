package com.webchat.netnest.Repository;

import com.webchat.netnest.entity.imageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<imageEntity, Long> {
}
