## Inflearn : 스프링 입문 - 코드로 배우는 스프링 부트, 웹 MVC, DB 접근 기술
- 스프링 공부하는 이유 : 실무에서 동작하는 웹앱 개발

### 라이브러리
  - Gradle은 의존관계가 있는 라이브러리를 함께 다운로드 한다
#### 스프링 부트 라이브러리
- spring-boot-starter-web
  - spring-boot-starter-tomcat: 톰캣 (웹서버)
    - 웹 서버 : HTTP를 통해 웹 브라우저에서 요청하는 HTML 문서나 오브젝트(이미지 파일 등)을 전송해주는 서비스 프로그램
      or 웹 서버 소프트웨어를 구동하는 하드웨어
  - spring-webmvc: 스프링 웹 MVC
- spring-boot-starter-thymeleaf: 타임리프 템플릿 엔진(View)
- spring-boot-starter(공통): 스프링 부트 + 스프링 코어 + 로깅
  - spring-boot
    - spring-core
  - spring-boot-starter-logging
    - logback, slf4j
#### 테스트 라이브러리
- spring-boot-starter-test
- junit: 테스트 프레임워크
- mockito: 목 라이브러리
- assertj: 테스트 코드를 좀 더 편하게 작성하게 도와주는 라이브러리
- spring-test: 스프링 통합 테스트 지원

### View 환경설정
#### Welcome Page 만들기
- static/index.html 을 올려두면 [Welcome page](https://docs.spring.io/spring-boot/docs/2.3.1.RELEASE/reference/html/spring-boot-features.html#boot-features-spring-mvc-welcome-page) 기능을 제공한다.

#### thymeleaf 템플릿 엔진
- thymeleaf 공식 사이트: https://www.thymeleaf.org/
- 스프링 공식 튜토리얼: https://spring.io/guides/gs/serving-web-content/
- 스프링부트 메뉴얼: https://docs.spring.io/spring-boot/docs/2.3.1.RELEASE/reference/html/spring-boot-features.html#boot-features-spring-mvc-template-engines

동작 환경 <br>
<img width="611" alt="스크린샷 2021-08-11 오후 3 50 01" src="https://media.oss.navercorp.com/user/26171/files/d1c35580-fabb-11eb-9b0a-3a0c28b09755">
- 컨트롤러에서 리턴 값으로 문자를 반환하면 뷰 리졸버( viewResolver )가 화면을 찾아서 처리한다.
  - 스프링 부트 템플릿엔진 기본 viewName 매핑
  - resources:templates/ +{ViewName}+ .html

정적 컨텐츠 그림<br>
<img width="613" alt="스크린샷 2021-08-11 오후 3 52 08" src="https://media.oss.navercorp.com/user/26171/files/1e0e9580-fabc-11eb-89ec-6d55dfc1ed4f">
- html 요청 들어오면 내장 톰캣 서버에서 Controller먼저 찾고, 없으면 resources: static/hello-static.html
<br>
MVC, 템플릿 엔진 그림<br>
<img width="607" alt="스크린샷 2021-08-11 오후 3 53 46" src="https://media.oss.navercorp.com/user/26171/files/57470580-fabc-11eb-944f-d85f62d68b58">

- html을 그냥 주는 게 아니라 프로그래밍해서 서버에서 html을 바꿔서 주는 게 템플릿 엔진

API
- @ResponseBody 문자 반환

```java

@Controller
public class HelloController {
 @GetMapping("hello-string")
 @ResponseBody
 public String helloString(@RequestParam("name") String name) {
 return "hello " + name;
 }
}
```

  - @ResponseBody 를 사용하면 뷰 리졸버( viewResolver )를 사용하지 않음
  - 대신에 HTTP의 BODY에 문자 내용을 직접 반환(HTML BODY TAG를 말하는 것이 아님)
  - 실행 : http://localhost:8080/hello-string?name=spring
  
- @ResponseBody 객체 반환
```java

@Controller
public class HelloController {
 @GetMapping("hello-api")
 @ResponseBody
 public Hello helloApi(@RequestParam("name") String name) {
 Hello hello = new Hello();
 hello.setName(name);
 return hello;
 }
 static class Hello {
 private String name;
 public String getName() {
 return name;
 }
 public void setName(String name) {
 this.name = name;
 }
 }
}
```
@ResponseBody 사용 원리<br>
<img width="609" alt="스크린샷 2021-08-11 오후 4 07 44" src="https://media.oss.navercorp.com/user/26171/files/d89f9780-fabe-11eb-852d-409202ec3523">

- @ResponseBody 를 사용하고, 객체를 반환하면 객체가 JSON으로 변환됨
- HTTP의 BODY에 문자 내용을 직접 반환
- viewResolver 대신에 HttpMessageConverter 가 동작
- 기본 문자처리: StringHttpMessageConverter
- 기본 객체처리: MappingJackson2HttpMessageConverter
- byte 처리 등등 기타 여러 HttpMessageConverter가 기본으로 등록되어 있음
- 클라이언트의 HTTP Accept 해더와 서버의 컨트롤러 반환 타입 정보 둘을 조합해서 HttpMessageConverter 가 선택


### 회원 관리 예제 

#### 비즈니스 요구사항 정리
- 데이터: 회원ID, 이름
- 기능: 회원 등록, 조회
- 아직 데이터 저장소가 선정되지 않음(가상의 시나리오)

일반적인 웹 애플리케이션 계층 구조<br>
<img width="608" alt="스크린샷 2021-08-11 오후 4 15 30" src="https://media.oss.navercorp.com/user/26171/files/5e234780-fabf-11eb-97e3-90304818f9bc">
- 컨트롤러: 웹 MVC의 컨트롤러 역할
- 서비스: 핵심 비즈니스 로직 구현
- 리포지토리: 데이터베이스에 접근, 도메인 객체를 DB에 저장하고 관리
- 도메인: 비즈니스 도메인 객체, 예) 회원, 주문, 쿠폰 등등 주로 데이터베이스에 저장하고 관리됨

클래스 의존관계<br>

<img width="50%" alt="스크린샷 2021-08-11 오후 4 17 46" src="https://media.oss.navercorp.com/user/26171/files/b1959580-fabf-11eb-8171-9044bdcb1d96">

- service class는 비즈니스 의존적 설계
- Repository는 개발적 설계






### Test
- 정상 플로도 중요하지만 예외 플로도 중요
- Test 매 항목마다 클리어 해 주는 것 필수
- @AfterEach는 매 테스트마다 실행해줌


#### Question
> 의문(0811 Q1) : 다음 코드에서 memberService의 MemoryMemberRepository는 memberRepository( line 2의 memberRepository)가 아니고 
> memberService.memberRepository( line 3의 memberRepository) 라고 생각하는데(물론 private라 . 으로 접근 못함)
> 왜 line2의 memberRepository가 line 1 의 memberService에 영향을 미치는 지 의문.
```java

class MemberServiceTest {


    MemberService memberService = new MemberService(); // line 1
    MemoryMemberRepository memberRepository = new MemoryMemberRepository(); // line 2

    @AfterEach
    public void afterEach() {
        memberRepository.clearStore();
    }
}

public class MemberService {

    private final MemberRepository memberRepository = new MemoryMemberRepository(); // line 3
}
    
```

> 해답(0811 Q1) : 고민하다가 모르겠어서 질문 적어 놓고 넘어가자 마자 강의에서 설명해 줌
> - MemoryMemberRepository의 Map 변수(변수명 store) 가 static으로 돼 있어서 다 동일한 store를 사용하기 때문인 듯(그래도 MemberRepository 객체가 따로 있는 건 바람직하지 않은 듯)
> - MemberService의 생성자의 인자로 외부에서 MemberRepository 받게 하고 
>   MemberServiceTest에서 @BeforeEach로 new MemberRepository 생성해서 MemberService에 생성자 인자로 넣어 줘서 해결. : Dependency Injection (DI) 이라고 함.


### 2021.08.12
### 스프링 빈과 의존관계
- MemberService 통해서 회원가입하고 MemberService 통해서 data 조회하는 등을 의존관계가 있다고 한다. : MemberController가 MemberService를 의존한다.
- 스프링이 관리를 하게 되면 다 spring container에 등록해서 관리하게 하고 다 받아서 쓰도록 해야 됨. (여러 Controller들이 같은 MemberService를 써야 하기 떄문)
- Controller를 통해서 외부 요청 받고, Service에서 비즈니스 로직 만들고, Repository에서 데이터 저장
- 스프링 빈을 등록하는 2가지 방법
  - Spring Container에 등록하면 공용으로 쓰고 + 여러 부가 효과
  - 컴포넌트 스캔
    - 스프링이 올라올 때 컴포넌트 관련 annotation(따라 가 보면 @Component) 있으면 객체로 생성해서 스프링 컨테이너에 등록, @Autowired로 연결
    - @Controller라는 annotation 있으면 spring 뜰 때 spring container에서 관리함
    - Service는 @Service, Repository는 @Repository 해 줘야 스프링 컨테이너 등록됨.
    - 생성자에 @Autowired 써 있으면 스프링이 Membercontroller 생성자에 스프링 컨테이너에 있는 MemberService랑 연결해 줌. : Dependency Injection
    - HelloSpringApplication 의 하위 package들은 스프링이 다 컴포넌트 스캔함.
    - 참고: 스프링은 스프링 컨테이너에 스프링 빈을 등록할 때, 기본으로 싱글톤으로 등록한다(유일하게 하나만
      등록해서 공유한다) 따라서 같은 스프링 빈이면 모두 같은 인스턴스다. 설정으로 싱글톤이 아니게 설정할 수
      있지만, 특별한 경우를 제외하면 대부분 싱글톤을 사용한다.
  - 자바 코드로 직접 스프링 빈 등록하기
```java

@Configuration
public class SpringConfig {

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }
    
    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }
}    

```
    - 여기서는 향후 메모리 리포지토리를 다른 리포지토리로 변경할 예정이므로, 컴포넌트 스캔 방식 대신에
      자바 코드로 스프링 빈을 설정하겠다.
    - 주의: @Autowired 를 통한 DI는 helloConroller , memberService 등과 같이 스프링이 관리하는
      객체에서만 동작한다. 스프링 빈으로 등록하지 않고 내가 직접 생성한 객체에서는 동작하지 않는다.
    - DI에는 필드 주입, setter 주입, 생성자 주입 이렇게 3가지 방법이 있다. 의존관계가 실행중에
      동적으로 변하는 경우는 거의 없으므로 생성자 주입을 권장
      
  - 회원 관리 예제 - 웹 MVC 개발
    - 등록
      - html <form> 태그는 값 입력 가능
        - <form action="/members/new" method="post">
        - <input type="text" id="name" name="name" placeholder="이름을
입력하세요">
      - 스프링이 @PostMapping의 메소드 String create의 인자 MemberForm form의 멤버변수 String name에 자동으로 입력받은 값을 넣어 준다.(setName을 통해)
      > 의문 : setName 메소드를 스프링에서 자동으로 인식해서 넣어주나??
    - 조회
      > model.addAttribute에서 model이 뭐였지?
      ```HTML
      <tbody>
       <tr th:each="member : ${members}">
         <td th:text="${member.id}"></td>
         <td th:text="${member.name}"></td>
       </tr>
      </tbody>
      ```
  > HTML 로직은 강의 다 듣고 프로젝트 세팅하면서 찾아 보자.
  
  
### 스프링 DB 접근 기술
- h2 최초 실행 시 DB 파일 만듦, 터미널로 권한 부여해야 함 ```chmod 755 h2.sh```
- 그 다음부터는 파일에 직접 접근하지 않고 소켓을 통해 접근
> 소켓이란?? 공부


> DB 잘 모르니까 따로 공부해야 됨 우선은 흐름만 파악
```sql
drop table if exists member CASCADE;
create table member
(
 id bigint generated by default as identity,
 name varchar(255),
 primary key (id)
);
```

> DB 접근기술쪽 보실때, JPA 이나 SPRING JDBC 부분이 나올거에요. 
> 저희쪽은 어플레케이션에서의 DB연동은 SPRING JDBC 방식을 선택하고 있는대요. 이 부분 왜 그런 선택을 했는지 고민해 보기.

### JDBC
- JDBC(Java Database Connectivity)는 자바에서 데이터베이스에 접속할 수 있도록 하는 자바 API
- JDBC는 데이터베이스에서 자료를 쿼리하거나 업데이트하는 방법을 제공
  
- 순수 JDBC
  ```java
  @Override
    public Member save(Member member) {
        String sql = "insert into member(name) values(?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, member.getName());
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                member.setId(rs.getLong(1));
            } else {
                throw new SQLException("id 조회 실패");
            }
            return member;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }
  ```
  
  - 보는 바와 같이 복잡함.
  - SpringConfig에서 MemberRepository의 구현체만 바꿔주고 다른 코드들은 손 댈 필요가 없는 게 OOP의 큰 장점
  
  - 개방-폐쇄 원칙(OCP, Open-Closed Principle) : 확장에는 열려있고, 수정, 변경에는 닫혀있다.
  - 스프링의 DI (Dependencies Injection)을 사용하면 기존 코드를 전혀 손대지 않고, 설정만으로 구현 클래스를 변경할 수 있다.
  - 회원을 등록하고 DB에 결과가 잘 입력되는지 확인하자. 데이터를 DB에 저장하므로 스프링 서버를 다시 실행해도 데이터가 안전하게 저장
  
  
  
### 스프링 통합 테스트
- 테스트 전용 DB 따로 관리 함
- DB는 트랜잭션이라는 개념이 있음
  - insert query 후에 commit 해 줘야 반영이 됨, 또는 autocommit 모드
  - @Transactional 하면 먼저 실행 후 테스트 끝나면 롤백
  - @SpringBootTest : 스프링 컨테이너와 테스트를 함께 실행한다.
  - @Transactional : 테스트 케이스에 이 애노테이션이 있으면, 테스트 시작 전에 트랜잭션을 시작하고,
    테스트 완료 후에 항상 롤백한다. 이렇게 하면 DB에 데이터가 남지 않으므로 다음 테스트에 영향을 주지
    않는다.
- MemberServiceTest처럼 자바만 돌리는 걸 순수한 단위 테스트가 MemberServiceIntegrationTest 같은 통합테스트보다 훨씬 좋을 확률이 높다
- 스프링 컨테이너 없이 테스트할 수 있도록 훈련

### 스프링 JDBC 
- 생성자가 하나만 있으면 @Autowired 생략 가능
- 순수 Jdbc와 동일한 환경설정을 하면 된다.
  스프링 JdbcTemplate과 MyBatis 같은 라이브러리는 JDBC API에서 본 반복 코드를 대부분
  제거해준다. 하지만 SQL은 직접 작성해야 한다.

> 테스트 코드 꼼꼼하게 잘 짜는 게 더 중요

### JPA
- 자바 퍼시스턴스 API또는 자바 지속성 API(Java Persistence API, JPA)
- 자바 플랫폼 SE와 자바 플랫폼 EE를 사용하는 응용프로그램에서 관계형 데이터베이스의 관리를 표현하는 자바 API이다.
- JPA는 기존의 반복 코드는 물론이고, 기본적인 SQL도 JPA가 직접 만들어서 실행해준다.
- JPA를 사용하면, SQL과 데이터 중심의 설계에서 객체 중심의 설계로 패러다임을 전환을 할 수 있다.
- JPA를 사용하면 개발 생산성을 크게 높일 수 있다.
<br>

### 20210813
- jpa는 자바 진영의 표준 Interface , 구현은 여러 업체들이 개별적으로 진행
- jpa는 객체랑 ORM이라는 기술
  - ORM : Object Relational(DB) Mapping

- 자바 ORM 기술에 대한 표준 명세로, JAVA에서 제공하는 API이다. 스프링에서 제공하는 것이 아님!
- ORM이기 때문에 자바 클래스와 DB테이블을 매핑한다.(sql을 매핑하지 않는다)

### JPA 엔티티 : 테이블에 대응하는 하나의 클래스
```java

@Entity
public class Account {
  String username;
  String password;
}

```
- CLass에 @Entity 붙이면 JPA가 관리하는 Entity가 됨
  - Pk : primary key
- DB에서 pk를 자동으로 넣어주는 걸 Identity 전략

- 만약 DB의 Column 이름이 username이면 멤버변수 name 선언 코드 앞에 @Column(name = “username”)으로 맵핑
- jpa는 entity manager라는 걸로 모든 것 동작
- Starter-data-jpa 받으면 스프링부트가 자동으로 현재 DB랑 다 연결해서 Entity manager생성해줌. 그래서 우리는 만들어진 걸 Injection받으면 됨
    - Property나 DB connection정보나 다 합쳐서 자동으로 Entity manager만들어줌. 내부적으로 DataSource를 다 들고 있어서 DB랑 통신하는 등 내부에서 다 처리함
    - Em.persist : 영구저장
    - em.find(Member.class, pk)

- JPQL : Java Persistence Query Language
  - 테이블 대상이 아닌 객체 대상 QL

- jpa 쓰려면 항상 @Transactional 있어야 함(데이터 저장 변경 시 항상 필요, 있으면 자동롤백)
- jpa는 쿼리 들어올 때 모든 데이터 변경이 TRansaction안에서 이뤄져야함

### 스프링 데이터 JPA
- 스프링 데이터 Jpa가 JpaRepository를 상속받고 있으면 구현체를 자동으로 만들어서 등록해줘서 구현 코드 따로 작성할 필요 없음
 
 
### AOP
- AOP : Aspect Oriented Programming == 관점 지향 프로그래밍
  - 관점 지향은 쉽게 말해 어떤 로직을 기준으로 핵심적인 관점, 부가적인 관점으로 나누어서 보고 그 관점을 기준으로 각각 모듈화하겠다는 것
  - 공통 관심 사항(cross-cutting concern) vs 핵심 관심 사항(core concern)


