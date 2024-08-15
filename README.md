# 공지사항 관리 REST API 구현

## 실행 환경
* APPLE MacOS - Sonoma 14.5
* intelliJ 2024.02
* Spring boot 3.3.3, openJDK 17, JPA, queryDSL, MySQL 8.3.0

----
## 참고사항
* 이번 프로젝트는 공지사항에 관련된 프로젝트이므로, 회원가입 등에 대한 자세한 설명, 단위테스트, 통합테스트는 서술하지 않습니다.
* 또한 위와 같은 이유로 회원 인증 시 토큰은 따로 사용하지 않았습니다.
* 화면에서 toast ui grid <https://ui.toast.com/tui-grid>, tui-pagination <https://ui.toast.com/tui-pagination>을 사용하였기 때문에 spring boot의 page, pageable을 사용하지 않고 구현했습니다.

----
## 환경 설정
* mac brew 설치
  * <https://brew.sh/ko/>
* openJDK 설치
  ```
  - brew install openjdk@17
  ```
* mysql 설치
  ```
    - brew install mysql
    - mysql --version
    - mysql.server start
  ```
  * mysql workbench 설치 <https://dev.mysql.com/downloads/workbench/>
  * 필요한 테이블은 /src/test/resources/schema.sql 참고
* redis 설치
```
    - brew install redis
    - redis-server
```

---
## 회원 가입 및 진행 방법
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
## 고려사항
### 대용량 트래픽
* pagination 사용
* redis 캐시 사용
 * serviceImpl에서 @Cacheable을 사용하여 공지사항 목록 조회 시 redis에 캐시 저장 
  ```
    @CacheEvict(value = "noticeList", allEntries = true)
  ```
 * 등록, 수정, 삭제 실행 시 위 코드의 @CacheEvict 어노테이션을 활용해서 캐시 초기화 진행
* database index 활용
 * 자주 사용하는 컬럼에 index를 활용합니다.
 * 이번 프로젝트에서는 공지사항을 최신순으로 보여준다는 가정 하에 created_at desc를 중심으로 검색 조건인 제목, 내용, 제목 + 내용 인덱스 생성
```
CREATE INDEX idx_notice_created_at ON notice.notice(created_at desc);
CREATE INDEX idx_notice_creator_created_at ON notice.notice(creator, created_at desc);
CREATE INDEX idx_notice_title_created_at ON notice.notice(title, created_at desc);
CREATE INDEX idx_notice_content_created_at ON notice.notice(content, created_at desc);
CREATE INDEX idx_notice_title_content_created_at ON notice.notice(title, content, created_at desc);
```
### 기타 고려사항 및 구현내용
* 첨부파일 등록 시 originalFileName()을 사용하는 경우, 서버에 업로드 될 때 중복이 발생할 수 있으므로 UUID를 사용하여 고유한 파일명 생성
* 공지사항 삭제 시 첨부파일도 삭제되어야 하므로 DB에서는 notice_attachment 테이블에 foreign key (notice_no) references notice(notice_no) on delete cascade를 사용하였고, 실제 파일은 파일 삭제 로직을 따로 구현
* 공지사항 상세 조회 시 notice, notice_attachment를 따로 분리해서 가지고 올 경우 api를 2번 호출하게 되기 때문에 NoticeDetailResponse를 만들어서 함께 반환하도록 구현
---
## 테스트
* 단위테스트는 /src/test/com/api/notice/NoticeServiceUnitTest.java, 통합테스트는 /src/test/com/api/notice/NoticeServiceIntegrationTest.java 실행
* 통합테스트는 테스트용 In-memory H2 database를 활용하여 생성
