# TutorLink

### 프로젝트 설명

온라인 과외 중개 서비스



### 팀원

노호준 (https://github.com/codeehh)



### 배포 환경

- AWS EC2 Linux

- Jenkins 자동 배포



### 기술 스택

- Java 11

- Spring Boot

- Spring Data JPA

- MySQL

- JWT

- Websocket

- Redis

- Rest Docs



### 구현 기능

##### 인증

- 소셜 로그인

- jwt 토큰 인증

##### 과외

- CRUD
- 조회 캐싱
- 검색 기능
- 좋아요 기능

##### 채팅

- 웹 소켓 채팅



### 기술적 경험

- 외부 api를 이용한 소셜 로그인 기능 개발
- LoginInterceptor를 통해 jwt 토큰을 검증 및 처리 로직 개발
- 메인 페이지 호출 데이터에 redis 캐싱 기능으로 조회 속도 향상
- 자주 조회되는 테이블의 칼럼에 인덱스 생성해서 조회 속도 향상
- 테스트 코드 작성으로 Rest Docs api 문서 생성
- aws linux 환경에 github, jenkins로 자동 배포
- 웹소켓과 STOMP를 이용한 채팅 구현



### ERD

![image-20230814115250820](assets/image-20230814115250820.png)

### API

<img src="assets/image-20230814122153923.png" alt="image-20230814122153923" style="zoom: 80%;" />
