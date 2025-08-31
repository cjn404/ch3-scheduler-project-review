package org.example.ch3schedulerprojectreview.user.repository;

import org.example.ch3schedulerprojectreview.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일 존재 유무 확인
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
