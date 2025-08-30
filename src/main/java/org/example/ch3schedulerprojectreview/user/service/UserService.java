package org.example.ch3schedulerprojectreview.user.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.ch3schedulerprojectreview.user.dto.UserRequest;
import org.example.ch3schedulerprojectreview.user.dto.UserResponse;
import org.example.ch3schedulerprojectreview.user.dto.UserUpdateRequest;
import org.example.ch3schedulerprojectreview.user.entity.User;
import org.example.ch3schedulerprojectreview.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 생성
    @Transactional
    public UserResponse save(UserRequest request) {
        User user = new User(
                request.getEmail(),
                request.getPassword(),
                request.getUserName()
        );
        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getUserId(),
                savedUser.getEmail(),
                savedUser.getUsername(),
                savedUser.getCreatedAt(),
                savedUser.getModifiedAt()
        );
    }

    // 조회
    @Transactional(readOnly = true)
    public UserResponse findById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User with id " + userId + " not found")
        );
        return new UserResponse(
                user.getUserId(),
                user.getEmail(),
                user.getUsername(),
                user.getCreatedAt(),
                user.getModifiedAt()
        );
    }

    // 수정
    @Transactional
    public UserResponse updateById(Long userId, UserUpdateRequest updateRequest) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User with id " + userId + " not found")
        );
        user.updateUser(
                updateRequest.getPassword(),
                updateRequest.getUserName()
        );
        return new UserResponse(
                user.getUserId(),
                user.getEmail(),
                user.getUsername(),
                user.getCreatedAt(),
                user.getModifiedAt()
        );
    }

    // 삭제
    @Transactional
    public void deleteById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User with id " + userId + " not found")
        );
        userRepository.delete(user);
    }
}
