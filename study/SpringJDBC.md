[Spring Jdbc 개념과 예시 코드](https://tech.junhabaek.net/infra-layer-with-spring-spring-jdbc-%EA%B0%9C%EB%85%90%EA%B3%BC-%EC%98%88%EC%8B%9C-%EC%BD%94%EB%93%9C-1c3f4e3ccb63)

### Spring JDBC
- Data Source를 설정을 통해 생성
  - Driver/DB 연결
  - Connection 객체의 관리 등을 수행
  - Spring Jdbc에서는 xml파일에 datasource에 대한 설정을 했을 때, DataSource Bean을 필요로 하는 부분에 주입할 수 있게 해줍니다.
- Connection Pool
  - 같은 연결을 재사용함으로서 요청 처리에 대한 오버헤드를 줄일 수 있도록
  - 연결 객체를 미리 여러개 만들어 둔 뒤, transaction에 진입할 때 하나의 연결 객체를 할당
  - 트랜잭션이 종료되면 연결 객체를 다시 반납
  - Spring boot Jdbc starter를 사용한다면 기본적으로 HikariCP를 사용하도록

JDBCTemplate
- sql문이나 스토어드 프로시저 등을 호출하기 위한 핵심 API
- 내부적으로 DataSource를 사용하도록 돼 있음.
  - 드라이버 연결이나 Connection Pool에 대한 관리를 알아서 처리
Repository 정의
- Spring Jdbc에서 DB 접근 작업을 구현할 때에는 JdbcTemplate에 정의된 기능들을 사용합니다
```java
@Component
public class ItemRepository {
    private JdbcTemplate jdbcTemplate;
	public ItemRepository(DataSource dataSource){
            this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
}
```

- spring boot에서 자동으로 bean에 등록해주는 DataSource를 이용해, jdbcTemplate 객체를 생성
- Spring Container가 ItemRepository를 생성할 때, 이미 등록되어 있는 Bean 중 DataSource 객체를 찾아 전달
- Spring JDBC가 제공하는 JDBCTemplate에는 queryForObject와 query(결과 여러개) 메서드 존재
  - 여러 종류의 overloading 메소드 제공

```java
// class table 1:1 대응 매핑
jdbcTemplate.queryForObject(sql, Item.class, itemId);
// Mapping 직접 구현시
jdbcTemplate.queryForObject(sql, itemRowMapper, itemId);
```

- 첫 번째는 class 정의 전달해서 sql 실행 결과를 일치하는 이름의 attribute로 맵핑해서 전달
- 두 번째는 rowMapper 정의해서 직접 mapping 기준 결정

Insert, Update, Delete
- insert, update, delete는 query method가 아니라 update method를 사용
