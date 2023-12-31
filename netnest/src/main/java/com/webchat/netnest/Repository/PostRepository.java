package com.webchat.netnest.Repository;

import com.webchat.netnest.entity.postEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<postEntity, Integer> {


}
