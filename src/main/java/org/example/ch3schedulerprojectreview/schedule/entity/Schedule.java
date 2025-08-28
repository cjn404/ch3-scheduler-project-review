package org.example.ch3schedulerprojectreview.schedule.entity;

import jakarta.persistence.*;
/** jakarta.persistence.*
 * JPA(자바 영속성 API)에서 제공하는 어노테이션과 클래스들을 가져옴
 */
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.ch3schedulerprojectreview.common.entity.BaseEntity;

@Entity
/** Entity
 * 정의: DB 테이블과 1:1로 매핑되는 클래스
 *      DB 행(row) 하나 = 엔티티 객체 하나
 *      즉 엔티티는 DB 데이터를 객체로 표현한 것
 * 역할: 1. 데이터 저장과 조회: 엔티티 객체를 통해 데이터를 JPA로 저장 또는 조회 가능
 *      2. JPA 영속성 관리
 *      : 영속성 컨텍스트에 의한 객체의 상태 관리
 *        더티 체킹(JPA가 객체의 변경을 감지해서 자동으로 DB를 업데이트)하여 DB에 자동 반영
 *        즉 JPA가 엔티티를 이용해 DB와 객체 간 가교 역할
 *      3. 상속을 통한 공통 속성 재사용
 * 영속 컨텍스트: JPA가 영속 상태의 엔티티 객체를 감시하는 저장소
 *            JPA는 객체와 스냅샷(조회 시 객체 상태 복제) 관리
 * 더티 체킹: 더티 = 변경된 데이터
 *          트랜잭션이 끝날 때(commit 또는 flush 시점) 영속 컨텍스트 안의 스냅샷과 현재 엔티티의 필드값 비교
 *          객체의 값 변경 시 -> 자동으로 UPDATE SQL을 만들어 DB에 반영
 *          영속 상태 엔티티 + 트랜잭션 내에서만 동작
 */
@Getter
/** @Getter
 * 클래스 내 모든 필드에 getter 메서드 자동 생성
 * 해당 어노테이션 덕분에
 * public Long getScheduleId() {
 *     return scheduleId;
 * }
 * 작성에서 해방 롬복 굿굿
 *
 * cf) Setter 안 쓰는 이유
 * 엔티티의 필드를 어디서든 수정 가능
 * -> 데이터 무결성 위반 & JPA 불변성 위반
 * -> 값은 엔티티를 생성하는 시점 또는 비즈니스 로직 메서드를 통해서만 변경 가능
 *    (생성자에서만 초기화)
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
/** @NoArgsConstructor
 * JPA 전용 기본 생성자 자동 생성
 * public Schedule() {
 * }
 * -> JPA는 DB에서 엔티티를 조회할 때 리플렉션으로 객체 생성
 * => 이때 기본 생성자가 반드시 필요. 없으면 어떤 값 생성해야 할지 몰라서 오류 발생
 *
 * access = AccessLevel.PROTECTED
 * : 기본 생성자의 접근 수준을 PROTECTED로 제한. 외부에서 무분별하게 new ...() 방지
 *   PROTECTED : 같은 패키지 또는 자식 클래스만 생성자 호출 가능
 */
public class Schedule extends BaseEntity {    // BaseEntity 상속 -> 생성일 및 수정일 필드 자동 포함

    @Id    // 해당 필드가 엔티티의 기본 키(PK)임을 명시
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /** @GeneratedValue
     * : 기본 키 값을 DB에서 자동 생성 설정
     *   전략을 안 쓰면 기본값은 AUTO
     *   -> JPA 구현체가 DB 벤더에 맞게 전략 자동 선택
     *      MySQL이면 IDENTITY를, Oracle이면 SEQUENCE를 내부적으로 선택
     *
     * strategy = GenerationType.IDENTITY
     * : DB의 AUTO_INCREMENT 기능을 사용하여 PK 값을 자동 증가
     */
    private Long scheduleId;
    /** 일정의 고유 식별자 저장 필드
     * 타입이 Long인 이유: 많은 데이터 저장
     */
    @Column(length = 100, nullable = false)
    private String title;
    /** 별도의 어노테이션이 없으면,
     * 필드명 그대로 컬럼명으로 사용 및 DB 테이블에 자동으로 매핑
     *
     * length : 문자열의 최대 길이
     *          String의 경우 기본값(255) 적용
     * -> @Column 붙이면 세부 설정 가능
     */
    @Column(length = 200)
    private String content;

//    @ManyToOne(fetch = FetchType.LAZY)    // 필요할 때만 DB 조회(해당 User 객체가 실제 사용될 때 SELECT 발생)
//    @JoinColumn(name = "user_id")    // 외래 키(FK) 매핑할 때 사용하는 JPA 어노테이션. FK 컬럼 이름 직접 지정
//    private User user;    // Schedule 엔티티에서 User 객체를 직접 참조

    /** 기본 생성자 -> 어노테이션으로 자동 생성
    public Schedule() {
    }
    */

    public Schedule(String title, String content) {    // 파라미터가 있는 생성자. @NoArgsConstructor와 함께 사용
        this.title = title;
        this.content = content;
    }

    public void updateSchedule(String title, String content) {
        this.title = title;
        this.content = content;
    }
    /**
     * 객체지향적 설계 원칙(OOP)에 부합
     * 엔티티 = DB 데이터와 1:1로 매핑되는 객체
     * 엔티티 내부에서 자기 상태를 변경하는 로직을 가지면 응집도 상승
     * 위 상태 변경이 영속 상태(Persistent) 객체 + 트랜잭션 내에서 더티체킹 적용되어 DB 자동 업데이트
     *
     * 트랜잭션(Transaction): DB에서 수행되는 작업들의 한 묶음
     *                     -> 모두 성공 또는 모두 실패해야만 함
     */

    /** getter -> 어노테이션으로 자동 생성
     public Long getScheduleId() {
         return scheduleId;
     }
     */
}
