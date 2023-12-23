package com.webchat.netnest.Repository;

import com.webchat.netnest.entity.status_user;
import com.webchat.netnest.entity.userEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatusRepository extends JpaRepository<status_user, Integer> {

    Optional<status_user>  findByUser(userEntity userId);
}
