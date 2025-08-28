#

## API 명세서

### 1. 인증 인가

| Method | Endpoint     | Description | Parameters	                        | Request Body	                                                     | Response	                                                                                                                     | Status Code    | Error Codes                                            |
 |--------|--------------|-------------|------------------------------------|-------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------|----------------|--------------------------------------------------------|
| POST   | /auth/signup | 회원가입        | 없음                                 | {"email" : String,<br>"userName" : String,<br>"password": String} | { "userId" : Long,<br>"email" : "String",<br>"userName" : "String",<br>"createdAt" : "datetime"<br>"modifiedAt" : "datetime"} | 201 CREATED    | 400 BAD REQUEST,<br>409 CONFLICT                       |
| POST   | /auth/login  | 로그인         | 없음                                 | {"email" : String,<br>"password" : String}                        | 없음                                                                                                                            | 204 No Content | 400 BAD REQUEST,<br>401 UNAUTHORIZED,<br>404 NOT FOUND |
| POST   | /auth/logout | 로그아웃        | {Authentication: Session (Cookie)} | 없음                                                                | 없음                                                                                                                            | 204 No Content  | 401 UNAUTHORIZED                                       |
| DELETE | /auth/me     | 회원탈퇴        | {Authentication: Session (Cookie)} | {"password" : String}                                             | 없음                                                                                                                            | 204 No Content  | 400 BAD REQUEST,<br>401 UNAUTHORIZED,<br>404 NOT FOUND |

### 2. 사용자

| Method | Endpoint  | Description | Parameters	                        | Request Body	                                | Response	                                  | Status Code | Error Codes                          |
|--------|-----------|-------------|------------------------------------|----------------------------------------------|--------------------------------------------|-------------|--------------------------------------|
| GET    | /users/me | 회원 조회       | {Authentication: Session (Cookie)} | 없음                                           | {"email" : String,<br>"userName" : String} | 200 OK      | 400 BAD REQUEST,<br>401 UNAUTHORIZED |
| PUT    | /users/me | 회원정보 수정     | {Authentication: Session (Cookie)} | {"userName" : String,<br>"password": String} | {"userName" : String}                      | 200 OK      | 400 BAD REQUEST,<br>401 UNAUTHORIZED |

### 3. 일정

| Method	 | Endpoint	               | Description	 | Parameters	                                                  | Request Body	                                                      | Response	                                                                                                                                              | Status Code   | Error Codes                    |
|---------|-------------------------|--------------|--------------------------------------------------------------|--------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------|---------------|--------------------------------|
| POST    | /schedules              | 일정 생성        | {Authentication: Session (Cookie)}                           | {"title" : "String",<br>"content" : String}                        | {"scheduleId" : String, "userName" : "String",<br>"title" : "String",<br>"content" : String,<br>"createdAt" : "datetime"<br>"modifiedAt" : "datetime"} | 201 CREATED   | 404 NOT FOUND,<br>409 CONFLICT |
| GET     | /schedules              | 일정 전체 조회     | {Authentication: Session (Cookie)}                           | 없음                                                                 | {"userName" : "String",<br>"title" : "String",<br>"content" : String,<br>"createdAt" : "datetime"<br>"modifiedAt" : "datetime"}                        | 200 OK        | 404 NOT FOUND                  |
| GET     | /schedules/{scheduleId} | 일정 단건 조회     | {Authentication: Session (Cookie)<br>path : Long scheduleId} | 없음                                                                 | {"userName" : "String",<br>"title" : "String",<br>"content" : String,<br>"createdAt" : "datetime"<br>"modifiedAt" : "datetime"}                        | 200 OK        | 404 NOT FOUND                  |
| PUT     | /schedules/{scheduleId} | 일정 수정        | {Authentication: Session (Cookie)<br>path : Long scheduleId} | {"password": String,<br>"title" : "String",<br>"content" : String} | {"userName" : "String",<br>"title" : "String",<br>"content" : String,<br>"createdAt" : "datetime"<br>"modifiedAt" : "datetime"}                        | 200 OK        | 404 NOT FOUND                  |
| DELETE  | /schedules/{scheduleId} | 일정 삭제        | {Authentication: Session (Cookie)<br>path : Long scheduleId} | {"password": String}                                               | 없음                                                                                                                                                     | 204 No Content | 404 NOT FOUND                  |

### 4. 댓글

| Method	 | Endpoint	                        | Description	 | Parameters	                                                  | Request Body	        | Response	                                                                                                                              | Status Code    | Error Codes                                            |
|---------|----------------------------------|--------------|--------------------------------------------------------------|----------------------|----------------------------------------------------------------------------------------------------------------------------------------|----------------|--------------------------------------------------------|
| POST    | /schedules/{scheduleId}/comments | 댓글 생성        | {Authentication: Session (Cookie)<br>path : Long scheduleId} | {"content" : "String"}                  | {"scheduleId" : "Long",<br>"commentId" : "Long",<br> "content" : "String",<br> "createdAt" : "datetime",<br>"modifiedAt" : "datetime"} | 201 CREATED    | 400 BAD REQUEST,<br>401 UNAUTHORIZED                   |
| GET     | /schedules/{scheduleId}/comments | 댓글 전체 조회     | {Authentication: Session (Cookie)<br>path : Long scheduleId} | 없음                   | {"scheduleId" : "Long",<br>"commentId" : "Long",<br> "content" : "String",<br> "createdAt" : "datetime",<br>"modifiedAt" : "datetime"} | 200 OK         | 401 UNAUTHORIZED                                       |
| PUT     | /comments/{commentId}            | 댓글 수정        | {Authentication: Session (Cookie)<br>path : Long commentId}  | 없음                   | {"commentId" : "Long",<br> "content" : "String",<br> "createdAt" : "datetime",<br>"modifiedAt" : "datetime"}                           | 200 OK         | 400 BAD REQUEST,<br>401 UNAUTHORIZED,<br>404 NOT FOUND |
| DELETE  | /comments/{commentId}            | 댓글 삭제        | {Authentication: Session (Cookie)<br>path : Long commentId}  | {"password": String} | 없음                                                                                                                                     | 204 No Content | 401 UNAUTHORIZED,<br>404 NOT FOUND                     |

<br>

## 테이블 명세서

### 1. Users 엔티티 명세서

| 필드명	        | 타입	            | 제약 조건	                  | 설명           |
|-------------|----------------|-------------------------|--------------|
| id	         | Long	          | PK, Auto Increment	     | 사용자 고유 식별자   |
| email	      | String	        | UK, Not Null	           | 이메일          |
| userName    | String		       | Not Null	               | 사용자명         |
| password	   | String	        | Not Null	               | 비밀번호(Bcrypt) |
| createdAt	  | LocalDateTime	 | Not Null, JPA Auditing	 | 생성일          |
| modifiedAt	 | LocalDateTime	 | Not Null, JPA Auditing	 | 수정일          |

### 2. Schedules 엔티티 명세서

| 필드명	        | 타입	            | 제약 조건	                  | 설명        |
|-------------|----------------|-------------------------|-----------|
| id	         | Long	          | PK, Auto Increment	     | 일정 고유 식별자 |
| userId	     | Long	          | FK(user.id), Not Null	  | 일정 작성자 ID |
| title	      | String	        | Not Null	               | 일정 제목     |
| content     | String		       | Optional	               | 일정 내용     |
| createdAt	  | LocalDateTime	 | Not Null, JPA Auditing	 | 생성일       |
| modifiedAt	 | LocalDateTime	 | Not Null, JPA Auditing	 | 수정일       |

### 3. Comments 엔티티 명세서

| 필드명	        | 타입	            | 제약 조건	                    | 설명          |
|-------------|----------------|---------------------------|-------------|
| id	         | Long	          | PK, Auto Increment	       | 댓글 고유 식별자   |
| userId	     | Long	          | FK(user.id), Not Null	    | 댓글 작성자 ID   |
| scheduleId  | Long           | FK(schedule.id), Not Null | 댓글 대상 일정 ID |
| content     | String		       | Not Null	                 | 댓글 내용       |
| createdAt	  | LocalDateTime	 | Not Null, JPA Auditing	   | 생성일         |
| modifiedAt	 | LocalDateTime	 | Not Null, JPA Auditing	   | 수정일         |




