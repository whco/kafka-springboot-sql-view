## 스프링 5 프로그래밍 입문 정리

### 2장 스프링 시작하기
- 스프링 핵심 기능 : 객체를 생성하고 초기화하는 기능 제공
- 스프링이 생성한 객체 : 빈(Bean) 객체
- @Configuration : 해당 클래스를 스프링 설정 클래스로 지정
- @Bean : 해당 메소드가 생성한 객체를 스프링이 관리하는 빈 객체로 등록
  - @Bean 붙인 메소드 이름은 빈 객체 구분 시 사용
  - @Bean 붙인 메소드는 객체 생성하고 초기화해야 함.
- AnnotationConfigApplicationContext : 자바 설정에서 정보 읽어와 빈 객체 생성, 관리
  - @Bean 안에서 설정된 객체 생성하고 초기화
- getBean() : 생성된 빈 객체 검색
  - 메소드 이름, 클래스 타입 으로 검색
- BeanFactory 인터페이스 : 객체 생성과 검색에 대한 기능 정의 ex) getBean()
  - 싱글톤/프로토타입 빈인지 확인하는 기능도 제공
- ApplicationContext 인터페이스 : 메시지, 프로필/환경 변수 처리하는 기능 추가 정의
  - (또는 BeanFactory가) 컨테이너임 : 빈 객체 생성, 초기화, 보관, 제거 등 관리( + 의존 주입 등 객체 관리 다양한 기능)
- AnnotationConfigApplicationContext, GenericXmlApplicationContext, GenericGroovyApplicationContext 등으로 설정 정보 가져옴
  -AnnotationConfigApplicationContext : 자바 어노테이션 이용한 클래스로부터 객체 설정 정보 가져옴

#### 싱글톤(Singleton) 객체
별도 설정 하지 않을 경우 @Bean 에 대해 한 개의 빈 객체만 생성 : "싱글톤 범위를 갖는다"

### 3장 스프링 DI
- 의존 : 한 클래스가 다른 클래스의 메소드 실행
- 변경에 의해 영향을 받는 관계
- DI : 의존하는 객체를 직접 생성 대신 전달받음.
- 장점은 변경의 유연함

스프링은 DI 지원하는 조립기
- assembler 대신 AnnotationConfigApplicationContext(AppCtx.class) 사용
- 세터 방식은 필요한 의존 객체 전달하지 않아도 빈 객체가 생성되기 때문에 NullPointerException 발생 가능성
- 스프링은 설정 클래스 그대로 사용하지 않음
- 설정 클래스 상속한 새로운 설정 클래스 만들어서 사용
- @Autowired는 자동 주입
  - 스프링 빈에 의존하는 다른 빈을 자동으로 주입
- @Import로 설정파일 합치기 가능
- 빈 객체 등록 안 해도 되는 경우
  - 스프링 컨테이너가 제공하는 관리기능(자동 주입, 라이프사이클 관리 등)이 필요 없고
  - getBean() 메소드로 구할 필요 없으면 

### 4장 의존 자동 주입
- @Autowired로 자동 주입
  - 생성자, 필드, 세터
    - 필드에 있으면 스프링이 해당 타입 빈 객체 찾아서 필드 할당
  - @Autowired 붙이면 설정 클래스에서 의존 주입 안 해도 됨
  - 해당 빈 객체 여러 개면 에러
    - @Qualifier 사용
    - @Qualifier 없으면 빈 이름을 한정자로 지정
    - @Autowired도 @Qualifier 없으면 필드나 파라미터 이름을 한정자로 사용
    - 상속 관계로 해당 클래스 여러 개여도 중복 에러 발생
    > @Bean 에만 @Qualifer를 @Autowired 필드나 파라미터 명으로 사용하는 경우 동작 X
  - @Autowired 의 필수 여부
    - @Autowired 붙인 타입에 해당하는 빈이 존재하지 않으면 익셉션 발생
    - 꼭 필요 없으면 required=false 로 하면 주입 안함(메소드 호출 X)
    - require = false 대신 Optional 사용 가능
    - @Nullable 하면 해당 빈 없어도 메소드는 호출해서 null 전달
- 자동 주입과 명시적 의존 관계
  - 설정 클래스에서 의존을 주입했는데 자동 주입 대상이면?
  - @Autowired가 설정 덮어 씀
  - 헷갈리니까 자동 주입 일관되게 적용하든지 하자
  
### 5장 컴포넌트 스캔
컴포넌트 스캔 : 스프링이 직접 클래스 검색해서 빈으로 등록해 주는 기능
  - 설정 클래스에 빈으로 등록하지 않아도 원하는 클래스를 빈으로 등록
  - 값 안 주면 클래스 이름 첫 글자 소문자로 바꾼 이름이 빈 이름.
  - 설정 클래스에 @ComponentScan 적용
  - basePackages 속성으로 스캔 범위 지정
  - 스캔 대상 클래스 중 @Component 붙으면 객체 생성해서 빈으로 등록
  - excludeFilters 로 제와가능
    - FilterType.REGEX, FilterType.ASPECTJ, annotation 등
    - 여러 필터 배열로 전달가능
  - 충돌 처리
    - 같은 클래스면 하나는 빈 이름 지정해서 충돌 회피
    - 설정 클래스에서 수동 등록한 빈과 컴포넌트 등록한 클래스가 같은 이름이면 수동 등록한 빈만 등록.
    - 이름 다르면 둘 다 등록
### 6장 빈 라이프사이크로가 범위
컨테이너 초기화와 종료
- ```AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AppContext.class)``` 시점에 스프링 컨테이너 초기화
- ```close()```로 종료
  - AbstractApplicationContext에 정의
  - 컨테이너 초기화 : 빈 객체 생성, 의존 주입, 초기화
  - 컨테이너 종료 :  빈 객체 소멸
  - InitilizingBean, DisposableBean 구현하면 초기화, 소멸 과정 커스터마이징 가능
  - @Bean에 initMethod, destroyMethod로 특정 메소드 지정 가능
  - 빈 범위 프로토타입으로 지정하면 빈 객체 구할 때마다 매번 새로운 객체 생성
    - @Bean 에  @Scope("prototype") 지정
    - 명시적 싱글톤은 @Scope("singleton")
    - 프로토타입 범위의 빈은 완전한 라이프사이클 따르지 않음
    - 빈 객체 생성, 프로퍼티 설정, 초기화 O, 빈 객체 소멸 X -> 빈 객체 소멸 코드 직접 처리
### 7장 AOP 프로그래밍
- aspectjweaver 모듈은 AOP 설정에 필요한 애노테이션 제공
```ExeTimeCalculator.java```
```java
public class ExeTimeCalculator implements Calculator{
    private Calculator delegate;

    public ExeTimeCalculator(Calculator delegate) {
        this.delegate = delegate;
    }

    @Override
    public long factorial(long num) {
        long start = System.nanoTime();
        long result = delegate.factorial(num);
        long end = System.nanoTime();
        System.out.printf("%s.factorial(%d) 실행 시간 = %d\n",
                delegate.getClass().getSimpleName(),
                num, (end - start));
        return result;
    }
}
```
```java
public class MainProxy {

    public static void main(String[] args) {
        ExeTimeCalculator ttCal1 = new ExeTimeCalculator(new ImpeCalculator());
        System.out.println(ttCal1.factorial(20));

        ExeTimeCalculator ttCal2 = new ExeTimeCalculator(new RecCalculator());
        System.out.println(ttCal2.factorial(20));

    }
}
```
- 기존 코드 변경 없이 실행 시간 출력 가능
- 실행 시간 구하는 코드 중복 제거
  - 밀리 초 대신 나노 초 사용할 때 ExeTimeCalculator만 변경하면 됨
가능한 이유
- factorial() 기능 자체 직접 구현하기보단 다른 객체에 factorial()의 실행을 위임
- 계산 외의 다른 부가적 기능 실행
프록시 : 핵심 기능의 실행은 다른 객체에 위임하고 부가적인 기능을 제공하는 객체
- 대상 객체 : 실제 핵심 기능을 실행하는 객체
- 엄밀히는 위 코드는 데코레이터 객체에 가까움
  - 데코레이터는 기능 추가와 확장에 초점
  - 프록시는 접근 제어 관점에 초점
- 핵심 기능은 구현하지 않음
- 여러 객체에 공통으로 적용할 수 있는 기능을 구현
- AOP의 핵심 : 핵심 기능 구현과 공통 기능 구현을 분리
#### AOP
AOP : Aspect Oriented Programming
- 여러 객체에 공통으로 적용할 수 있는 기능을 분리해서 재사용성을 높여주는 프로그래밍 기법
- Aspect는 관점 보다는 기능, 관심으로 해석 권장
- 기본 개념
  - 핵심 기능에 공통 기능 삽입
  - 핵심 기능 코드 수정 없이 공통 기능 구현 추가
  - 공통 기능 삽입 방법
    - 컴파일 시점에 코드에 공통 기능 삽입
    - 클래스 로딩 시점에 바이트 코드에 공통 기능 삽입
      - 스프링 일부 지원
    - 런타임에 프록시 객체 생성해 공통 기능 삽입
      - 스프링 방식
  - 공통 기능 : Aspect
  - 스프링 AOP는 프록시 객체 자동으로 생성해 줌
    - 공통 기능 구현한 클래스만 구현
    
![AOP](https://media.oss.navercorp.com/user/26171/files/fd889e00-1587-11ec-8bf0-34d3c586672e)

AOP 주요 용어 정리
- Advice : 언제 공통 관심 기능 적용할 지 정의 ex) 메소드 호출하기 전에 트랜잭션 시작
- Joinpoint : Advice 적용 가능한 지점 - 메소드 호출, 필드 값 변경 등(스프링에선 메소드 호출만 지원)
- Pointcut : 실제 Advice가 적용되는 Joinpoint
- Weaving : Advice를 핵심 로직 코드에 적용하는 것
- Aspect : 여러 객체에 공통으로 적용되는 기능. ex) 트랜잭션, 보안
Advice 종류
  - Around Advice : 대상 객체 메소드 실행 전, 후 또는 익셉션 발생 시점에서 공통 기능 실행
    - 다양한 시점에 원하는 기능 삽입 가능
    - 캐시 기능, 성능 모니터링 기능 등
스프링 AOP 구현
- 절차
  - Aspect로 사용할 클래스에 @Aspect
  - @Pointcut으로 공통 기능 적용할 Pointcut 정의
  - 공통 기능 구현한 메소드에 @Around
- 구현
  - @Around의 값에 있는 메소드에 @Pointcut 지정
  - @Pointcut은 공통 기능 적용할 대상
    - @Pointcut("execution(public * chap07..*(..))")
    - chap07 패키지 및 하위 속한 빈 객체의 public 메소드에 @Around가 붙은 measure() 메소드를 적용한다
  - ProceedingJoinPoint 객체가 대상 객체
    - joinPoint.proceed()로 대상객체 메소드 실행
    - getSignature(), getTarget(), getArgs() : 호출한 메소드의 시그니처, 대상 객체, 인자 목록
  - 설정 클래스에 @EnableAspectJAutoProxy 하면 @Aspect 찾고 @Pointcut, @Around 설정 사용
  > @Enable 류 : 관련 기능을 적용하는데 필요한 다양한 스프링 설정 대신 처리
- 프록시 생성 방식
  - 스프링은 AOP를 위한 프록시 객체 생성할 때 실제 생성할 빈 객체가 인터페이스를 상속하면 인터페이스 이용해서 프록시 생성
  > @Pointcut이 메소드를 대상으로 한다 했는데 어떻게 생성자에서부터 프록시를 생성??
  - 자바 클래스 상속받아 프록시 생성하고 싶으면 @EnableAspectJAutoProxy(proxtyTargetClasee = true)
  - execution 명시자 표현식
    - 기본 형식 : execution(수식어패턴? 리턴타입패턴 클래스이름패턴?메소드이픔패턴(파라미터패턴))
    - 수식어패턴 : 생략 가능, public, protect 중 스프링 AOP는 public 메소드에만 적용 가능
    - 리턴타입패턴 : 리턴 타입
    - 클래스이름패턴, 메소드이름패턴 : 클래스 이름, 메소드 이름 패턴
    - 파라미터패턴 : 매칭될 파라미터
    - * 은 모든 값 표현. .. 은 0개 이상
  - Advice 두 개 이상 적용 가능
    - calculator 빈은 CacheAspect 프록시 객체
      - CacheAspect 프록시 객체의 대상 객체는 ExeTimeAspect의 프록시 객체
      - ExeTimeAspect 프록시의 대상 객체가 실제 대상 객체
    - @Order 로 순서 지정 가능
      - 내 버전에선 AppCtx에 지정한 순서만 영향 미치는 듯
  - @Around Poincut 설정과 @Pointcut 재사용
    - @Around에 직접 execution 지정 가능
    - @Pointcut에 execution 지정해서 재사용 가능
    
### DB 연동
이 책은 JdbcTemplate 설명<br>
Jdbc 프로그래밍의 단점을 보완하는 스프링
- 템플릿 메소드 패턴, 전략 패턴 함께 사용
> 디자인 패턴!!
- 트랜잭션 관리 쉬움
  - Connection의 setAutoCommit(false)
    - JDBC API 사용
    - commit(), rollback()
    - @Transactional 사용
- 커넥션 풀
  - 일정 개수의 DB 커넥션을 미리 만들어 두는 기법
    - 최초 연결 시(커넥션 생성) 시간, 동접자 많을 시 사용자마다 커넥션 생성해서 DBMS에 부하
  - Tomcat JDBC, HikariCP, DBCP 등에서 제공
- DataSource 설정
  - DataSource를 스프링 빈으로 등록
  - DB 연동 기능 구현한 빈 객체는 DataSource 주입 받아 사용
- query, queryForObject
  - queryForObject는 결과가 한 행일 때
- 변경 쿼리
  - update() 사용
#### @Transactional
- 설정
  - PlatformTransactionManager 빈 설정
  - @Transactional 활성화 설정
  - 디폴트로는 RuntimeException에서 롤백
  - 이외 익셉션 롤백 지정 원할 시 @Transactional(rollbackFor = {SQLException.class, IOException.class ...})
  - noRollbackFor
- 주요 속성
  - propagation 기본값 : Propagation.REQUIRED
  - islolation 기본값 : Isolation.DEFAULT
    - org.springframework.transaction.annotation 패키지 정의
  - timeout 기본값 : -1(DB 타임아웃)
- 트랜잭션 전파
  - JdbcTemplate은 진행 중인 트랜잭션 존재하면 해당 트랜잭션 범위에서 쿼리 실행
