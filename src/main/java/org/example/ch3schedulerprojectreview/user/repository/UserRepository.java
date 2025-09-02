package org.example.ch3schedulerprojectreview.user.repository;

import org.example.ch3schedulerprojectreview.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {    // User = 엔티티 클래스. Long = 엔티티의 PK 타입. -> DB 테이블과 1:1 매핑

    boolean existsByEmailAndDeletedFalse(String email);
    /** 이메일 존재 유무 확인 -> 삭제되지 않은(Deleted = false) 사용자만 검색
     * JPA가 메서드 명을 해석하여 SQL 자동 생성(메서드 명 기반 쿼리 생성, 커스텀 메서드)
     *
     * existBy: 조건을 만족하는 테이터가 존재하는지만 확인
     * Email: User 엔티티의 email 필드 기준
     * And: 조건 연결
     * DeletedFalse: deleted 필드 값이 false인 데이터만 조회
     *
     * 최종 SQL
     * -> SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END
     *    FROM user
     *    WHERE email = ? AND deleted = false;
     *
     * - CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END
     *   의미 : COUNT(*) > 0이면(조건을 만족하는 데이터가 하나라도 있으면) TRUE를 반환
     *         그렇지 않으면(0개라면) FALSE를 반환
     *
     * - ?(바인딩 파라미터)
     *   의미: 쿼리를 실행 시 실제 값(메서드 인자)으로 치환
     *        하드코딩하지 않고 외부에서 값을 안전하게 주입하기 위해 사용
     */

    Optional<User> findByEmailAndDeletedFalse(String email);
    /** 이메일로 사용자 정보 조회 및 삭제되지 않은 사용자만 검색
     * 결과가 없을 수 있으므로 Optional로 감싸 null 처리 방지
     *
     * findBy: 조회
     * Email: User 엔티티의 email 필드
     * And: 조건 연결
     * DeletedFalse: deleted 필드 값이 false인 데이터
     *
     * 최종 쿼리문
     * -> SELECT *
     *    FROM user
     *    WHERE email = ? AND deleted = false;
     */
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
