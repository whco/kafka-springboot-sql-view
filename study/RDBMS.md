[네이버셰어 RDBMS 강의](https://share.navercorp.com/de-1/joinLectures/75866) 정리

데이터모델링 : 현실세계의 업무 데이터를 담는 메타 데이터를 설계하는 작업
- 엔티티 : DB가 표현하려고 하는 정보 대상 - 단독으로 존재
  - 사람 집 학생 인사 급여 등
- 속성 : 이름을 가진 정보의 가장 작은 논리적인 단위
  - 전화번호 성 이름 등
- 관계 : 개체들 간의 의미있는 연결 또는 연관성
  - 관계도 하나의 개체로 간주 가능
  - 강의 등록 주문 배송 등
ERD(Entity Relational Diagram)

### SQL
SQL
- 원하는 결과 집합을 표현하는 언어
- 동일한 결과 집합을 표현할 수 있는 방법은 무수히 많다.
- 사용자가 RDBMS와 통신할 수 있는 유일한 방법
- 4GL 언어로 직관적이나 내부동작을 알 수 없다
  - 4GL?? : 4th Generation Language
- HOW가 아닌 WHAT을 기술(결과집합이 무엇인가를 기술)
  
  
SELECT
- 조회, CRUD의 R
- 문법 작성 순서
  - SELECT -> FROM -> WHERE -> GROUP BY -> HAVING -> ORDER BY
- 쿼리 처리 순서
  - FROM -> WHERE -> GROUP BY -> SELECT -> ORDER BY -> LIMIT
    - SELECT 문을 사용할 때 출력되는 결과물은 테이블에 입력된 순서대로 출력되는 것이 기본
    
  - <img width="579" alt="스크린샷 2021-08-20 오후 5 40 28" src="https://media.oss.navercorp.com/user/26171/files/e13e4f00-01dd-11ec-9c64-e5367ae1a4fa"><br>
  - MySQL 실행 순서: FROM -> ON -> JOIN -> WHERE -> GROUP BY -> CUBE | ROLLUP -> HAVING -> SELECT -> DISTINCT -> ORDER BY -> TOP
- 주의점
  - SELECT절에는 조회하고자 하는 컬럼 명시(SELECT * FROM 금지)
  - FROM 절은 3depth 이상 들어가지 않도록 자제
  - Alias는 직관적으로 명시
  - SELECT절에 있는 subquery는 자제
INSERT
- 입력, CRUD의 C
- 5가지 종류의 QUERY (MySQL 기준 작성)

  - <img width="643" alt="스크린샷 2021-08-20 오후 5 57 18" src="https://media.oss.navercorp.com/user/26171/files/177cce00-01e0-11ec-9106-10d0cc782db6"><br>
  
    - 컬럼 개수랑 인자 수 일치
    - SELECT절 성능 확인
    - REPLACE는 데이터 삭제 후 INSERT
    - INSERT ON DUPLICATE KEY UPDATE는 원하는 컬럼의 데이터 수정
- 대량 INSERT 작업이 있는  batch job의 경우 주기적으로 commit 수행
  - table lock 가능성
UPDATE
- 수정, CRUD의 U
- UPDATE SET WHERE 세트

- <img width="692" alt="스크린샷 2021-08-20 오후 6 17 23 1" src="https://media.oss.navercorp.com/user/26171/files/e520a000-01e2-11ec-8ae4-521dff035eb7"><br>
- 주의점
  - WHERE 절 있는 지 확인
  - UPDATE 대상 맞는지 확인 (SELECT로 한번 더 수행)
  - 불안하면 ```start transaction; update ... set ... where; commit; 또는 rollback;``` 을 수행
  - DML작업은 DBA가 지원 안함
  - 대량 작업의 경우 DBA에게 모니터링 요청
  > DML(Data Manipulation Language) :  데이터베이스 사용자 또는 응용 프로그램 소프트웨어가 컴퓨터 데이터베이스에 대해 데이터 검색, 등록, 삭제, 갱신을 위한, 데이터베이스 언어 또는 데이터베이스 언어 요소
  > DBA(DB Administrator) : 한 조직 내에서 데이터베이스를 설치, 구성, 업그레이드, 관리, 감시하는 일을 맡은 사람
DELETE
- 삭제, CRUD의 D
- <img width="680" alt="스크린샷 2021-08-20 오후 6 45 12" src="https://media.oss.navercorp.com/user/26171/files/c4f2e000-01e6-11ec-9656-57889c0fe533"><br>
  - TRUNCATE 와 DELETE FROM 차이
    - TRUNCATE TABLE
      - drop & create와 동일(디스크 공간 반납)
      - auto_increment 값 초기화
    - DELETE FROM TABLE
      - 실제 디스크 공간 반납 안 함
      - auto_increment 값 유지
    - 주의점
      - WHERE 절 있는 지 확인
      - DELETE 대상 맞는지 확인 (SELECT로 한번 더 수행)
      - 불안하면 ```start transaction; delete from; commit; 또는 rollback;``` 을 수행
      - DML작업은 DBA가 지원 안함
      - 대량 작업의 경우 DBA에게 모니터링 요청
      
JOIN
  - 다른 이블의 열을 가지고 와서 열을 늘리는 집합 연산
  - union은 행 결합, join은 열 결합
  - 종류
    - 내부조인(Inner Join) 조건을 사용해서 두 테이블의 레코드를 결합
    - 외부조인(Outer Join) 내부조인과 비슷한데 일치하지 않는 열까지 반환하며 그 열은
      NULL로 반환
    - 동등 조인(Equi Join) 내부조인으로, 두 테이블 사이의 같은 행들을 반환
    - 비동등 조인(Non-EquiJoin) : 내부조인으로, 두 테이블 사이의 같지 않은 행등을 반환
    - 자연 조인(Natural Join) ‘on’ 절이 없는 내부조인으로 같은 열 이름을 가진 두 테이블을 조
      인할 때만 작동
    - 크로스 조인(Cross Join) 한 테이블의 모든 행과 다른 테이블의 모든 행이 연결되는 모든 경
      우를 반환
    - 카티젼 조인(Catesian Join) 크로스 조인의 한 종류로, 조건이 없음
    - 셀프 조인(Self Join) 자기 자신을 조인
