package org.example.ch3schedulerprojectreview.user.service;

import lombok.RequiredArgsConstructor;
import org.example.ch3schedulerprojectreview.common.exception.custom.ConflictException;
import org.example.ch3schedulerprojectreview.common.exception.custom.NotFoundException;
import org.example.ch3schedulerprojectreview.common.exception.custom.UnauthorizedException;
import org.example.ch3schedulerprojectreview.config.PasswordEncoder;
import org.example.ch3schedulerprojectreview.schedule.entity.Schedule;
import org.example.ch3schedulerprojectreview.schedule.repository.ScheduleRepository;
import org.example.ch3schedulerprojectreview.user.dto.*;
import org.example.ch3schedulerprojectreview.user.entity.User;
import org.example.ch3schedulerprojectreview.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
/**
 * 비즈니스 로직(Business Logic)을 처리하는 서비스 클래스임을 나타냄
 * 스프링이 Bean으로 자동 등록
 */
@RequiredArgsConstructor
/**
 * final 필드에 대한 생성자 자동 생성
 * 코드 없이도 생성자를 만들어서 userRepository, passwordEncoder 자동 DI(Dependency Injection) 가능
 */
public class UserService {

    // 의존성 주입: Repository와 비밀번호 암호화 도구를 사용
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ScheduleRepository scheduleRepository;

    /** @RequiredArgsConstructor이 없으면,
     * public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
     *     this.userRepository = userRepository;
     *     this.passwordEncoder = passwordEncoder;
     *     }
     */

    /*
    Hibernate:
    select
    u1_0.user_id
            from
    user u1_0
    where
    u1_0.email=?
    and not(u1_0.deleted)
    limit
        ?
    Hibernate:
    insert
            into
    user
            (created_at, deleted, email, modified_at, password, username)
    values
            (?, ?, ?, ?, ?, ?)
    */

    // 회원가입
    @Transactional
    /**
     * 메서드 내 DB 작업을 트랜잭션 단위로 묶음
     * 정상 종료 -> 커밋, 예외 발생 -> 롤백
     */
    public UserResponse signup(UserRequest request) {
        // "이메일"만 중복 여부 확인
        if (userRepository.existsByEmailAndDeletedFalse(request.getEmail())) {
            throw new ConflictException("이미 사용 중인 이메일입니다.");    // 409
        }

        // 평문 비밀번호 -> 해시 변환
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 유저 생성
        User user = new User(
                request.getEmail(),
                encodedPassword,
                request.getUserName()
        );
        userRepository.save(user);
        /**
         * 엔티티 생성 후 DB 저장
         * JPA 영속성 컨텍스트에 관리됨 -> 더티 체킹 가능
         */
        return new UserResponse(
                user.getUserId(),
                user.getEmail(),
                user.getUsername(),
                user.getCreatedAt(),
                user.getModifiedAt());
    }

    /*
    Hibernate:
    select
    u1_0.user_id,
    u1_0.created_at,
    u1_0.deleted,
    u1_0.email,
    u1_0.modified_at,
    u1_0.password,
    u1_0.username
            from
    user u1_0
    where
    u1_0.email=?
    and not(u1_0.deleted)
    */

    // 로그인
    @Transactional(readOnly = true)    // 읽기 전용 트랜잭션 -> DB에 SELECT만, 변경 감지 없음
    public UserResponse login(UserLoginRequest loginRequest) {
        /** existsByEmail vs findByEmail
         * existsByEmail 사용 시: 이메일 유무 여부만 확인 가능
         *                      -> 비밀번호 확인 시 User 객체 필요
         *                      => DB 쿼리 2번(exists + findByEmail) 실행하게 되는 셈
         * findByEmail 사용 시: DB 쿼리 1번으로 이메일 유무 여부 확인 + User 객체 가져오기 가능
         */
        User user = userRepository.findByEmailAndDeletedFalse(loginRequest.getEmail()).orElseThrow(
                () -> new NotFoundException("해당하는 계정이 없습니다.")    // 404
        );
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("비밀번호가 일치하지 않습니다.");    // 401
        }

        return new UserResponse(
                user.getUserId(),
                user.getEmail(),
                user.getUsername(),
                user.getCreatedAt(),
                user.getModifiedAt());
    }

    /*
    Hibernate:
    select
    u1_0.user_id,
    u1_0.created_at,
    u1_0.deleted,
    u1_0.email,
    u1_0.modified_at,
    u1_0.password,
    u1_0.username
            from
    user u1_0
    where
    u1_0.user_id=?
    Hibernate:
    update
            user
    set
    deleted=?,
    email=?,
    modified_at=?,
    password=?,
    username=?
    where
    user_id=?
    */

    // 회원탈퇴 - 소프트 딜리트 처리해야 해서 비즈니스 로직 필요
    @Transactional
    public void withdraw(Long userId, UserWithdrawRequest withdrawRequest) {
        User user = userRepository.findById(userId).orElseThrow(    // 세션에서 가져온 유저 ID로 DB 조회
                () -> new NotFoundException("해당하는 계정이 없습니다.")    // 404
        );
        if (!passwordEncoder.matches(withdrawRequest.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("비밀번호가 일치하지 않습니다.");    // 401
        }
        // 유저의 모든 스케줄 조회 * -> 유저가 스케줄을 반복문
        // 조건을 특정해서 쿼리문 줄이기 -> update schedule s set s.isDeleted = ture where s.user_id = 1; -> 새로 만든다. 쿼리메서드
        List<Schedule> schedules = scheduleRepository.findByUserUserIdAndDeletedFalse(userId);
        for (Schedule schedule : schedules) {
            schedule.softDelete();  // Schedule deleted = true
        }
        // Soft Delete
        user.softDelete();
        /**
         * deleted = true
         * 소프트 딜리트 -> DB row 삭제는 안 하고 deleted 컬럼만 true로 변경
         * 더티 체킹으로 자동 UPDATE
         */
//        userRepository.save(user);
    }

    /*
    Hibernate:
    select
    u1_0.user_id,
    u1_0.created_at,
    u1_0.deleted,
    u1_0.email,
    u1_0.modified_at,
    u1_0.password,
    u1_0.username
            from
    user u1_0
    where
    u1_0.user_id=?
    */

    // 조회
    @Transactional(readOnly = true)
    public UserResponse findMe(Long userId) {    // 세션 기반 ID로 회원 정보 조회 -> Response DTO 반환
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("해당하는 계정이 없습니다.")
        );
        return new UserResponse(
                user.getUserId(),
                user.getEmail(),
                user.getUsername(),
                user.getCreatedAt(),
                user.getModifiedAt()
        );
    }

    /*
    Hibernate:
    select
    u1_0.user_id,
    u1_0.created_at,
    u1_0.deleted,
    u1_0.email,
    u1_0.modified_at,
    u1_0.password,
    u1_0.username
            from
    user u1_0
    where
    u1_0.user_id=?
    Hibernate:
    update
            user
    set
    deleted=?,
    email=?,
    modified_at=?,
    password=?,
    username=?
    where
    user_id=?
    */

    // 수정
    @Transactional
    public UserResponse updateMe(Long userId, UserUpdateRequest updateRequest) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("해당하는 계정이 없습니다.")
        );
        if (!passwordEncoder.matches(updateRequest.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("비밀번호가 일치하지 않습니다.");
        }
        // 비밀번호 수정으로 평문 입력된 비밀번호 해시화 * 영속 컨텍
        String plainPassword = updateRequest.getPassword();
        String encodedPassword = passwordEncoder.encode(plainPassword);

        user.updateUser(
                encodedPassword,
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
