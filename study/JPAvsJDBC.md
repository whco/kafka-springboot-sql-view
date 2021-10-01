## JPA 및 JDBC 비교 정리
[참조](https://velog.io/@bread_dd/Spring-Data-JDBC-vs-Spring-Data-JPA)

- Spring Data JPA는 Java ORM 표준 JPA와 Hibernate로 만들어진 프레임워크
  - ORM(Object-relational mapping, 객체 관계 매핑) : 데이터베이스와 객체 지향 프로그래밍 언어 간의 호환되지 않는 데이터를 변환하는 프로그래밍 기법.
    객체 지향 언어에서 사용할 수 있는 "가상" 객체 데이터베이스를 구축하는 방법
  - 복잡한 특징들이 있어 러닝 커브가 높다고 함
    - Lazy Loading (Exception)
      - 코드만 보고 파악하기 힘든 Exception입니다. 코드가 복잡해질수록 지연 로딩으로 가져오는 객체가 어느 시점에 초기화되는지 알기 힘듭니다. 또한 코드만으로 다음에 어떤 SQL 문이 발생할지 예상하기 힘들다
    - Dirty Checking
      - Entity Manager에 save를 하는데, 원치 않은 save가 발생할 수 있습니다. 그냥 테스트를 위해 객체를 조작하고 싶어도, 저장까지 되는 문제가 발생합니다. 특정 객체만을 제외하고 저장하고 싶은 경우에도 복잡한 원리를 이해해야만 합니다.
    - Session / 1st Level Cache
      - 영속성 컨텍스트로 인해 항상 같은 객체만을 사용합니다. DB로부터 불러온 데이터와 조작한 객체를 가지고 비교하는 연산 등을 힘들게 합니다.
    - Proxy
      - 엔티티 간 비교 시 프록시인지 아닌지를 확인해야만 하는 경우가 생깁니다. 이 경우 어떤 객체가 프록시이고 아닌지 판단하기 어려움.
    - M to x Mapping
      - M 대 N 연관관계는 다양한 문제를 야기하고 예측할 수 없습니다.

- JDBC
  - JDBC의 특징은 단순함
    - No lazy loading
      - 항상 즉시 로딩 전략
      - Aggregate는 항상 완전한 상태
    - No Session(Caching)
    - No Proxies
    - No Flushing : save와 동시에 Query (쓰기 지연 저장소가 없음)
    - No Many to x Relation
      - 일대다 혹은 일대일 단방향 관계만을 추구
      -  ID 참조로 피해감.
  - Domain Driven Design을 기반
    - 모든 Repository는 Aggregate Root 기준으로 존재
    - 라이프사이클 또한 Aggregate Root와 하위 속성들이 동일
    - 서로 다른 Aggregate 간 참조는 Id를 통해 수행


<br>

  ![스크린샷 2021-08-25 오후 5 11 06](https://media.oss.navercorp.com/user/26171/files/82a31780-05c7-11ec-983d-f9992ce33f7d)
  
<br>

- JDBC 원리
  - 여러 드라이버들은 DBMS를 다룰 수 있는 코드를 가지고 있음
  - JDBC는 자바와 드라이버들을 연결하여 단일화된 인터페이스 제공
  - close 함수를 Statement, Connection, ResultSet에 모두 하여야 함
  
  ```java
  Class.forName("jdbc:mysql://dev-internship01-npay-ncl:3306/mydb"); // 난 이거 안했는데 왜 되지?
  Connection con = DriverManager.getConnection(...);
  Statement st = con.createStatement();
  ResultSet rs = st.executeQuery(sql); // 받으면 레코드 단위로 하나씩 받게 됨 : BoF부터 시작, 커서 or 포인터
  rs.next();
  String title = rs.getString("title");
  ```
  
- Spring 애플리케이션을 개발하다보면, 도메인 기반 디자인으로 설계를 하게 됩니다. 예를 들면, 회원 정보에 대한 부분은 Member 도메인을 설계하여, 그에 해당하는 Repository를 생성하고, 비즈니스 로직은 Service에서 처리하며, 클라이언트의 요청은 Controller에서 처리하는 MVC 패턴


- Spring JDBC
  - 스프링에서 DB를 사용하기 위한 오리지날 디펜던시
  - XML을 이용해 의존성 주입을 한 후 사용하는 방식
  - 다음을 자동으로 처리
    - Connection을 열고 닫기
    - Statement를 준비하고 닫기
    - Statement의 실행
    - ResultSet 반복 처리
    - 예외 처리 반환
    - Transaction 처리
