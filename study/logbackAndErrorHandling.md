<img width="811" alt="스크린샷 2021-09-02 오후 6 09 58" src="https://media.oss.navercorp.com/user/26171/files/0d63c200-0c19-11ec-8837-332d32c48f11">

현재 카프카에서 LOGGER.info 메소드를 사용해서 수신한 data의 value를 찍어 보고 있다.
에러가 발생했는데 그 에러가 어떤 orderNo에서 발생했는 지가 명확하지 않다.

<img width="1374" alt="스크린샷 2021-09-02 오후 6 15 00" src="https://media.oss.navercorp.com/user/26171/files/b5798b00-0c19-11ec-9f84-4d2877e0e3b7">

다음 메소드로 출력한 바로 위의 메시지가 같은 항목이라 막연히 추측했지만
출력된 에러의 바로 위 로그가 해당 에러와 동일한 항목이라는 확신이 없다.
```java
LOGGER.info("consumed data : " + data.value());
```

에러 핸들링을 위해 로그백을 통한 로그 출력과 트라이 캐치를 통한 익셉션 처리를 다룰 것이다.

[스프링 부트 Log 설정 - Logback](https://goddaehee.tistory.com/206) 참조
[스프링 부트 공식 문서 로깅](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.logging)

### Logback
- 자바 오픈소스 로깅 프레임워크,  SLF4J의 구현체
  - SLF4J : Simple Logging Facade For Java
    - Facade : 퍼사드 패턴은 소프트웨어 공학 디자인 패턴 중 하나이다. 객체 지향 프로그래밍 분야에서 자주 쓰인다. Facade 는 "건물의 정면"을 의미한다. 퍼사드는 클래스 라이브러리 같은 어떤 소프트웨어의 다른 커다란 코드 부분에 대한 간략화된 인터페이스를 제공하는 객체
  - 기본 로깅 백엔드는 클래스 경로에 원하는 바인딩을 추가하여 런타임 에 결정
  - 표준 Sun Java 로깅 패키지 java.util.logging, log4j , logback, tinylog 등
- Logger, Appender, Encoder
  - appender
    - log의 형태를 설정, 로그 메시지가 출력될 대상을 결정하는 요소(콘솔에 출력할지, 파일로 출력 할지 등의 설정)
  - root, logger
    - 설정한 appender를 참조하여 package와 level을 설정
  - layout, encoder
    - Layout : 로그의 출력 포맷을 지정
    - encoder : Appender에 포함되어 사용자가 지정한 형식으로 표현 될 로그메시지를 변환하는 역할을 담당하는 요소
   encoder는 바이트를 소유하고 있는 appender가 관리하는 OutputStream에 쓸 시간과 내용을 제어
   > ~~encoder를 설정해 에러와 consumed message를 같이 찍어야 할 것으로 예상~~
   try catch로 로그 

- 스프링부트 스타터에서 Logback이 디폴트, 라우팅도 돼 있음

- 로그 레벨 순서 및 사용방법
  - TRACE  <  DEBUG  <  INFO  <  WARN  <  ERROR
    - 1) ERROR : 요청을 처리하는 중 오류가 발생한 경우 표시한다.
    - 2) WARN  : 처리 가능한 문제, 향후 시스템 에러의 원인이 될 수 있는 경고성 메시지를 나타낸다.
    - 3) INFO  : 상태변경과 같은 정보성 로그를 표시한다.
    - 4) DEBUG : 프로그램을 디버깅하기 위한 정보를 표시한다. 
    - 5) TRACE : 추적 레벨은 Debug보다 훨씬 상세한 정보를 나타낸다. 
- application.properties에 ```ogging.level.root=info``` 와 같이 설정
- 하위 패키지들에 대한 각각의 로깅 레벨을 별도로 설정
```
logging.level.com.god.bo.test=info
logging.level.com.god.bo.test.controller=debug
```
