## 서블릿과 서블릿 컨테이너
[서블릿과 서블릿 컨테이너란?](https://steady-coding.tistory.com/462)
[서블릿 컨테이너란](https://velog.io/@han_been/%EC%84%9C%EB%B8%94%EB%A6%BF-%EC%BB%A8%ED%85%8C%EC%9D%B4%EB%84%88Servlet-Container-%EB%9E%80)


### 서블릿
- Java를 이용하여 웹 페이지를 동적으로 생성하는 서버측 프로그램


### 서블릿 컨테이너

<br>

![image](https://media.oss.navercorp.com/user/26171/files/34ec5900-0b4b-11ec-8b8a-1d2d975eff97)

<br>


- Servlet 클래스의 규칙에 맞게 서블릿 객체를 생성, 초기화, 호출 종료하는 생명 주기를 관리
- 서블릿 객체는 빈과 마찬가지로 싱글톤으로 관리
  - 싱글톤 : 소프트웨어 디자인 패턴에서 싱글턴 패턴을 따르는 클래스는, 생성자가 여러 차례 호출되더라도 실제로 생성되는 객체는 하나이고 최초 생성 이후에 호출된 생성자는 최초의 생성자가 생성한 객체를 리턴
- 통신 지원
  - 서블릿은 개발자가 비즈니스 로직에 집중할 수 있도록 해당 과정을 모두 자동으로
  - 서블릿 컨테이너는 서블릿과 웹서버가 손쉽게 통신할 수 있게 해주어, 소켓을 만들고 listen, accept 등을 API로 제공하여 복잡한 과정을 생략할 수 있게 해준다
  - 서블릿 컨테이너는 Clinet의 Request를 받아주고 Response할 수 있게, 웹 서버와 소켓을 만들어 통신합니다.

- 대표적으로 무료 서비스인 Tomcat(톰캣)이 있습니다.
  - 톰캣은 웹 서버와 소켓을 만들어 통신하며 JSP(java server page)와 Servlet이 작동할 수 있는 환경을 제공
- 멀티 스레딩 관리
  - 서블릿 컨테이너는 요청이 올 때 마다 새로운 자바 쓰레드를 하나 생성
  - HTTP 서비스 메소드를 실행하고 나면 쓰레드는 자동으로 종료
  - 원래는 쓰레드를 관리해야 하지만 서버가 다중 쓰레드를
생성 및 운영해주니 쓰레드의 안정성에 대해서 걱정하지 않아도 됩니다.
- 선언적인 보안 관리
  - 서블릿 컨테이너는 보안 관련된 기능을 지원하므로 서블릿 코드 안에 보안 관련된 메소드를 구현하지 않아도 됩니다.
- JSP 지원

