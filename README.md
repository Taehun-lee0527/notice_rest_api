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

---
# 회원 가입 및 진행 방법
### 회원 가입
* 프로젝트 실행 후 <localhost:8080/login> 접속
* 회원가입 버튼 클릭 후 계정 생성
### 공지사항 게시판
* <localhost:8080/notice> 이동
 * 공지사항 조회 ~~/api/notices~~
 * 게시판 등록
