package org.example.ch3schedulerprojectreview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
/** @EnableJpaAuditing
 * JPA Auditing 기능 활성화
 * Auditing 리스너(@CreatedDate, @LastModifiedDate 등)를 등록하고 동작하도록 허용
 */
@SpringBootApplication
public class Ch3SchedulerProjectReviewApplication {

    public static void main(String[] args) {
        SpringApplication.run(Ch3SchedulerProjectReviewApplication.class, args);
    }

}
