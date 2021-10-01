## TroubleShooting

- 토이프로젝트
  - 스프링
  - 카프카
  ```
     2021-08-26 16:09:32.745  WARN 14570 --- [ntainer#0-0-C-1] org.apache.kafka.clients.NetworkClient   : [Consumer clientId=consumer-foo-1, groupId=foo] Connection to node 0 (/192.168.0.26:9092) could not be established. Broker may not be available.
     ```
     
  - SQL
    - JDBC connection, statement, resultset 만들어서 카프카에서 받은 메시지 insert하는 메소드 에러남.
  
  
- 카프카
> ```ERROR Exiting Kafka. (kafka.Kafka$)``` 뜨면서 카프카 서버 시작 안 됨
> 해결 : 터미널 전부 종료 후 재시작

### 고민할 문제
- 기능별, 모듈별, 단위별 테스트 구축
- 카프카
  - 카프카 메시지 수신에 에러 발생 시 에러 메시지 재처리 문제
    - 실패 시나리오 구상 및 해결책
      - 시나리오
        1. 브로커에서 잘못된 메시지 형식 저장돼 있음
        2. 
      - 해결책
        - 고정된 딜레이로 메시지 처리 재시도
        - 실패한 메시지 복사본을 다른 토픽으로 전송하고 다음 메시지 기다림
          - retry_topic, failed_topic 
        - SeekToCurrentErrorHandler를 통해서 메시지 처리 재시도, 최대 실패수 도달 시 Recoverer 동작 설정 가능
        - DeadLetterPublishingRecoverer : 실패한 메시지 다른 토픽으로 보냄
  - 컨슈머 개수 늘려야 하는 지 : 3개로 구성할 듯
  - 레코드 리스너, 배치 리스너 중 무엇 선택할 지
  - Acknowledge 리스너(매뉴얼 커밋), ConsumderAware 리스너(컨슈머 인스턴스 직접 접근 컨트롤) 등 무엇 선택할 지
    - 커밋 종류 알아 보기
      - 기본 : 오토 커밋, 동기 커밋, 비동기 커밋
      - 스프링 카프카 : 7 종류(ackMode), 기본값 배치
      - 배치로 처리할 때 어떤 이슈가 있을 지   
  
- 효율적인 SQL 테이블 구조
  - 스키마
    - 정규화
    - 기본 키를 내가 설정한 임의의 id에서 nidNo로 바꿔야 하나?
    - 웹에서 정보 요청 시 어떻게 하면 효율적일지
  - 자료형이나 자료형 () 안의 숫자 결정 기준
  
- SQL 스키마를 변경해야 될 때 기존 메시지를 어떻게 처리할 것인지
  - 변경 시나리오 구상 및 해결책
  
- 패키지 클래스 메소드 역할에 따른 정리와 네이밍

- 웹 뷰 띄워 줄 때 SQL 쿼리
  - nidNo 기준으로 주문정보 선택해서 띄움
  - ```SELECT * FROM order_info WHERE nidNo = 'abc12'```
  - table에서 자바 객체로 만들어서 웹 띄울 때 정보 전달


### 20210902
- 로그 메시지 : 기존의 메시지 출력에는 어떤 메시지 때문에 에러가 발생했는 지 출력되지 않음

- 해결 : try catch 문으로 catch 절에 Logger로 data.value() 출력



### 20210903
- JSON 파싱 간소화 : 

기존 코드 
```java
JSONParser parser = new JSONParser();
Object obj = parser.parse((String) data.value());
JsonParse jp = new JsonParse(obj);

String orderEventType = jp.jsonParse("orderEventType").toString();
String nidNo = jp.jsonParse("memberInfo").jsonParse("nidNo").toString();
Long orderNo = jp.jsonParse("order").jsonParse("orderNo").toLong();
String orderStatus = jp.jsonParse("order").jsonParse("orderStatusCode").jsonParse("name").toString();
Long orderAmount = jp.jsonParse("order").jsonParse("orderAmount").jsonParse("number").toLong();
Long orderYmdt = jp.jsonParse("order").jsonParse("orderYmdt").jsonParse("date").toLong();

OrderInfo orderInfo = new OrderInfo(orderEventType, nidNo, orderNo, orderStatus, orderAmount, orderYmdt);
```

수정한 코드 

```java
ObjectMapper objectMapper = new ObjectMapper();
OrderInfo orderInfo = objectMapper.readValue(data.value().toString(), OrderInfo.class);
```
### 20210906
- consume data truncated error
> consume cause exception : org.springframework.dao.DataIntegrityViolationException: PreparedStatementCallback; SQL [INSERT INTO ORDER_INFO VALUES(null, ?, ?, ?, ?, ?, ?)]; Data truncated for column 'orderEventType' at row 1; nested exception is java.sql.SQLException: Data truncated for column 'orderEventType' at row 1

카프카로 메시지를 소비하는 도중 처음 만들었을 때부터 MySQL ENUM 변수형이 문제였다. <<br>
처음에는 제공받은 메시지 명세표를 보고 ENUM 타입으로 들어오는 'orderEventType'을 명세표의 항목들만으로 구성해 ENUM으로 생성하였다.<br>
실제로 카프카에서 메시지를 수신하니 'CLAIM_REQUESTED', 'CLAIM_REJECTED' 등 추가해야 할 항목이 생겼다.<br>
이는 ```Data truncated for column 'orderEventType' at row 1``` 라는 에러 메시지를 출력하였다.<br>
초기 개발 단계이니 당장의 해결을 위해 테이블을 초기화하고 orderEventType ENUM에 'CLAIM_REQUESTED'를 추가해 테이블 스키마를 다시 구성하였다.<br>
잘 되는 듯하다가 'CLAIM_REQUESTED'가 없어 에러가 나는 것을 확인햇다.<br>
마찬가지로 테이블을 초기화하거나 ```ALTER TABLE ORDER_INFO CHANGE orderEventType orderEventType ENUM'CLAIM_REQUESTED','CLAIM_COMPLETED', ...);``` 식으로 매번 처리할 수도 있지만 <br> 이넘 리스트 중간에 값을 추가하는 작업은 해당 테이블 전체를 뒤져야 될 것으로 예상된다.(굳이 중간에 추가할 이유가 없긴 하다. 보기 편한 이유라면 조회할 때는 정렬해서 조회하면 된다)
또한 ENUM 칼럼에 정의된 값들을 다른 테이블에서 사용할 수 없고 다른 DBMS와의 호환성도 떨어지므로 이에 ENUM 대신 문자열을 사용하거나 참조테이블을 사용하는 것을 고려해 보았다. <br>
변수형을 문자열로 변경 시 기존의 ENUM 사용의 목적인 '허용되지 않은 항목이 들어왔을 때 에러 처리' 를 달성할 수 없지만 추가적인 항목이 생겼을 때 별도의 처리를 하지 않아도 된다.<br>
참조테이블 사용 시 orderEventType이나 orderStatus 등의 ENUM 타입의 속성과 연관된 속성이 생길 시 쉽게 연관지을 수 있다. 예를 들어 제품의 색상을 enum으로 설정한 후 해당 색상이 선택 불가능하다면 색상 테이블에 is_available 항목을 추가해 주는 식이다. orderEventType이나 orderStatus에서 연관지어야 할 만한 항목이 생길 지 의문이다.<br>
참조 테이블을 사용하면 다른 테이블에서 재사용이 가능하다.<br>
우선은 위와 같은 장단점에도 ENUM은 String 상태 그대로 넣어주면 되는 장점이 있고 불필요한 테이블 생성을 지양하므로 ENUM을 유지하다 성능 issue나 또 다른 체감되는 불편함이 생기면 그 때 다시 한 번 생각하기로 결정했다.<br>
> 에러 메시지는 orderStatus가 'PAY_WATING'으로 들어오기 때문인 것을 확인했다. 
심각한 문제는 아니지만 enum 또는 참조 테이블로 한 덕분에 프로듀서가 오타를 전송하고 있음을 확인했다. 이런 경우 어떻게 처리해야 될 지 생각해 보겠다.


