package org.example.ch3schedulerprojectreview.user.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.ch3schedulerprojectreview.user.dto.UserLoginRequest;
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

    // 회원가입
    @Transactional
    public UserResponse signup(UserRequest request) {
        // "이메일"만 중복 여부 확인
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EntityNotFoundException("Email already exists");
        }

        // 유저 생성
        User user = new User(
                request.getEmail(),
                request.getPassword(),
                request.getUserName()
        );
        userRepository.save(user);
        return new UserResponse(
                user.getUserId(),
                user.getEmail(),
                user.getUsername(),
                user.getCreatedAt(),
                user.getModifiedAt());
    }

    // 로그인
    @Transactional(readOnly = true)
    public UserResponse login(UserLoginRequest loginRequest) {
        /** existsByEmail vs findByEmail
         * existsByEmail 사용 시: 이메일 유무 여부만 확인 가능
         *                      -> 비밀번호 확인 시 User 객체 필요
         *                      => DB 쿼리 2번(exists + findByEmail) 실행하게 되는 셈
         * findByEmail 사용 시: DB 쿼리 1번으로 이메일 유무 여부 확인 + User 객체 가져오기 가능
         */
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(
                () -> new EntityNotFoundException("User not found")
        );
        return new UserResponse(
                user.getUserId(),
                user.getEmail(),
                user.getUsername(),
                user.getCreatedAt(),
                user.getModifiedAt());
    }

    // 회원탈퇴 - 소프트 딜리트 처리해야 해서 비즈니스 로직 필요
    @Transactional
    public void withdraw(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User with id " + userId + " not found")
        );
        userRepository.delete(user);
    }

    // 조회
    @Transactional(readOnly = true)
    public UserResponse findMe(Long userId) {
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
    public UserResponse updateMe(Long userId, UserUpdateRequest updateRequest) {
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

}
