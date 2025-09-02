package org.example.ch3schedulerprojectreview.schedule.repository;

import org.example.ch3schedulerprojectreview.schedule.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    // 페이징, 정렬 지원
    Page<Schedule> findByUserUserIdAndDeletedFalse(Long userId, Pageable pageable);    // Schedule = 엔티티 클래스. Long = 엔티티의 PK 타입. -> DB 테이블과 1:1 매핑
    /** 최종 SQL
     * SELECT s.*
     * FROM schedule s
     * JOIN user u ON s.user_id = u.user_id
     * WHERE u.user_id = ?
     *   AND s.deleted = false
     * ORDER BY s.schedule_id ASC   -- Pageable에서 지정된 정렬 기준
     * LIMIT ? OFFSET ?             -- Pageable의 size와 page에 따라 동적으로 변함
     */

    Optional<Schedule> findByScheduleIdAndDeletedFalse(Long scheduleId);
    /** 최종 SQL
     * SELECT *
     * FROM schedule
     * WHERE schedule_id = ?
     *   AND deleted = false
     * LIMIT 1
     */

    // 전체 리스트 조회용
    List<Schedule> findByUserUserIdAndDeletedFalse(Long userId);
}
/** 인터페이스로 선언된 이유:
 * JpaRepository 기능 상속을 받음으로써 기본 메서드를 자동 제공받아 메서드 직접 구현할 필요 X
 * -> 아니면 SQL을 직접 작성하거나 CRUD 메서드를 직접 구현해야 함
 * JPA가 런타임에 내부적으로 Repository 인터페이스를 구현한 객체(프록시 객체) 자동 생성 및 Bean 등록
 *
 * 프록시(Proxy) 객체:
 * - 프록시(Proxy): 대리인
 *                직접 작성한 객체(Repository 구현체)가 없으므로, JPA가 메서드 동작을 대신 처리하는 가짜 객체를 동적으로 생성
 *                메서드 호출이 오면 중간에서 동작으로 가로채고 내부적으로 실제 동작(JPA 쿼리 실행 등) 처리한 뒤 결과 반환
 *                ex) findAll() 호출 시, JPA가 자동으로 SQL 실행
 * 빈(Bean) 등록: 객체를 스프링 컨테이너에 등록(다른 클래스에서 @Autowired로 주입 가능)
 */