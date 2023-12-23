package com.webchat.netnest.Repository;

import com.webchat.netnest.entity.commentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  CommentRepository extends JpaRepository<commentEntity, Integer> {
}
