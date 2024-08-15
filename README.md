# 실행 환경
* APPLE MacOS - Sonoma 14.5
* intelliJ 2024.02
* MySQL 8.3.0
* Spring boot 3.3.3, openJDK 17, JPA, queryDSL

----
# 환경 설정
* mac brew 설치
  * <https://brew.sh/ko/>
* openJDK 설치
  * brew install openjdk@17
* mysql 설치
  ```
    brew install mysql
    mysql --version
    mysql.server start
  ```
  * mysql workbench 설치 <https://dev.mysql.com/downloads/workbench/>
  * 필요한 테이블은 /src/test/resources/schema.sql 참고
* redis 설치
```
    brew install redis
    redis-server
```

---
# 회원 가입 및 진행 방법
### 회원 가입
* 프로젝트 실행 후 <localhost:8080/login> 접속
* 회원가입 버튼 클릭 후 계정 생성
### 공지사항 게시판
* <localhost:8080/notice> 이동
 * 공지사항 조회 **GET /api/notices**
 * 공지사항 상세 조회 **GET /api/notices/{noticeNo}**
 * 공지사항 등록 **POST /api/notices**
 * 공지사항 수정 **PUT /api/notices/{noticeNo}**
 * 공지사항 삭제 **DELETE /api/notices**
 * **각 api 상세 파라메터는 NoticeRestController 참고**

---
# 고려사항
### 대용량 트래픽
* redis 캐시 사용
 * serviceImpl에서 @Cacheable을 사용하여 공지사항 목록 조회 시 redis에 캐시 저장
 * 주의 사항
  * redis 캐시 적용 시 등록, 수정, 삭제가 발생할 경우
  ```
    @CacheEvict(value = "noticeList", allEntries = true)
  ```
   어노테이션을 활용해서 캐시 초기화를 진행한다.
* database index 활용
 * 자주 사용하는 컬럼에 index를 활용한다.
 * 이번 프로젝트에서는 공지사항을 최신순으로 보여준다는 가정 하에 created_at desc를 중심으로
검색 조건인 제목, 내용, 제목 + 내용
