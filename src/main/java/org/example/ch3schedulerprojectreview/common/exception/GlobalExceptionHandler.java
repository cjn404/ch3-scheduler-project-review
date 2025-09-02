package org.example.ch3schedulerprojectreview.common.exception;

import jakarta.persistence.EntityNotFoundException;
import org.example.ch3schedulerprojectreview.common.exception.custom.ConflictException;
import org.example.ch3schedulerprojectreview.common.exception.custom.NotFoundException;
import org.example.ch3schedulerprojectreview.common.exception.custom.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(NotFoundException e) {
        Map<String,String> map = new HashMap<>();    // * 일반적으로 맵을 쓰진 않고, "공통 응답 포맷"을 만드는 편(즉 예외에 대한 공통 응답 코드가 필요. 성공과 실패 상황을 구분해서 공통 응답 케이스를 만들기도 한다) -> 협업시 프론트엔드의 편의성을 위해
        map.put("errorStatus", "NOT FOUND");
        map.put("message",e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, String>> handleUnauthorized(UnauthorizedException u) {
        Map<String,String> map = new HashMap<>();
        map.put("errorStatus", "UNAUTHORIZED");
        map.put("message",u.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Map<String, String>> handleConflictException(ConflictException c) {
        Map<String,String> map = new HashMap<>();
        map.put("errorStatus", "CONFLICT");
        map.put("message",c.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(map);
    }
}
