## 카프카 컨슈머 exactly once 구현 위한 방법 조사 및 설계
클라이언트, 브로커 모두 11버전 이상만 지원

- exactly-once delivery는 프로듀서부터 컨슈머로 연결되는 파이프라인 처리
- 프로듀서에서 트랜잭션 처리로 exactly-once 구현해도 컨슈머에서 중복처리 로직은 따로 작성해야 함

트랜잭션 프로듀서일 떄는 오토 커밋 false, isolation level 을 read_committed 로 보내는 등이 가능한데 우선 브로커에 정확히 적재되었다고 가정.



컨슈머가 소비하는 두 가지 방법
- 1. subscribe로 소비 시 리밸런싱 자동으로 발생
  - (a) subscribe 두 번째 인자로 리스너 전달. 카프카는 리밸런싱 일어나면 리스너에게 알려줌. 리스너는 컨슈머에게 오프셋 수동 관리 기회 제공.
  - (b) 두 번째 인자 전달 X
- 2. assign 으로 소비 시 자동 리밸런싱 X


- Exactly-once Kafka Consumer
  - (1.a) 사용
  - 순서
    1. enable.auto.commit = false
    2. subscribe 메소드로 구독
    3. ConsumerRebalacneListener 구현해서 consumer.seek(topicPartition, offset) 수행
    4. 오프셋 DB 등에 원자적 트랜잭션으로 저장
    5. 멱등성 구현
  - (2) 사용
    1. enable.auto.commit = false
    2. assign 메소드로 특정 파티션 할당
    3. 시작 시 consumer.seek(topicPartition, offset)으로 찾기
    4. 오프셋 DB 등에 원자적 트랜잭션으로 저장
    5. 멱등성 구현
    
    
위 방법들 실습 후 스프링 카프카에 적합한 방법으로 개선 


