package org.example.ch3schedulerprojectreview.schedule.repository;

import org.example.ch3schedulerprojectreview.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByUserUserId(Long userId);    // Schedule = 엔티티 클래스. Long = 엔티티의 PK 타입. -> DB 테이블과 1:1 매핑
}
/** 인터페이스로 선언된 이유:
 * 기능 상속 받음으로 메서드 구현이 없어도 됨 -> 아니면 모든 CRUD 메서드 직접 구현해야 함
 * JPA가 런타임에 내부적으로 자동으로 Repository 인터페이스를 구현한 객체(프록시 객체) 생성 및 Bean 등록
 *
 * 프록시(Proxy) 객체: 실제 구현 대신 중간에서 동작으로 가로채고 처리해주는 객체
 *                  ex) findAll() 호출 시, JPA가 자동으로 SQL 실행
 * 빈(Bean) 등록: 객체를 스프링 컨테이너에 등록(다른 클래스에서 @Autowired로 주입 가능)
 */
