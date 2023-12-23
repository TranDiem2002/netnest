package com.webchat.netnest.Repository;

import com.webchat.netnest.entity.userEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<userEntity, Integer> {

    Optional<userEntity> findByEmail(String userMail);
}
