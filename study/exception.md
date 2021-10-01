[참조 : 자바 예외 구분: Checked Exception, Unchecked Exception](https://madplay.github.io/post/java-checked-unchecked-exceptions)

### Exception, Error
- Exception : 예외(Exception)란 입력 값에 대한 처리가 불가능하거나, 프로그램 실행 중에 참조된 값이 잘못된 경우 등 정상적인 프로그램의 흐름을 어긋나는 것
  - 자바에선 개발자가 직접 처리 가능
- Error : 시스템에 무엇인가 비정상적인 상황이 발생한 경우.
  - 주로 JVM에서 발생, 코드에서 못 잡음
  
### 예외 구분

  ![image](https://media.oss.navercorp.com/user/26171/files/ec7de100-100b-11ec-85c8-054a1a2eaefa)

RuntimeException을 상속하지 않는 클래스는 Checked Exception, 반대로 상속한 클래스는 Unchecked Exception

RuntimeException과 이를 상속한 클래스를 조금 특별하게 취급한다. 명시적으로 예외 처리를 하지 않아도 됨

![image](https://media.oss.navercorp.com/user/26171/files/16370800-100c-11ec-9cf3-64ee164a41c7)

### 예외 처리
예외를 처리하는 방법에는 예외 복구, 예외 처리 회피, 예외 전환 방법이 있다.

예외 복구
- 예외 상황을 파악하고 문제를 해결해서 정상 상태로 돌려놓는 방법
- 예외를 잡아서 일정 시간, 조건만큼 대기하고 다시 재시도를 반복한다.
- 최대 재시도 횟수를 넘기게 되는 경우 예외를 발생

예외처리 회피

- 예외 처리를 직접 담당하지 않고 호출한 쪽으로 던져 회피하는 방법
- 그래도 예외 처리의 필요성이 있다면 어느 정도는 처리하고 던지는 것 권장
- 긴밀하게 역할을 분담하고 있는 관계가 아니라면 예외를 그냥 던지는 것은 무책임하다.

예외 전환
- 예외 회피와 비슷하게 메서드 밖으로 예외를 던지지만, 그냥 던지지 않고 적절한 예외로 전환해서 넘기는 방법
- 조금 더 명확한 의미로 전달되기 위해 적합한 의미를 가진 예외로 변경한다.
- 예외 처리를 단순하게 만들기 위해 포장(wrap) 할 수도 있다.
