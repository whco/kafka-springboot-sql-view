## 카프카 유튜브 강의 기록입니다.

#### 20210819
카프카 개괄
- 카프카 프로듀서와 클라이언트 [하위호환성 체크](https://blog.voidmainvoid.net/193)
- replication 개수만큼 partition 존재
  - replication은 브로커 개수 이하
  - 원본 partition이 leader partition, 나머지는 follwer partition, 합쳐서 In Sync Replica(ISR)
  - ack = all 로 설정해 leader partition 및 follower partions에 데이터가 전달 되었는 지 확인.
  - 3개 이상의 broker 사용할 때 replication = 3 추천
- 카프카 컨슈머
  - 컨슈머가 데이터 가져가도 사라지지 않음
  - 컨슈머가 하는 역할 3가지
    - 데이터 가져오는 것을 폴링 : DB에 저장하거나 다른 파이프라인에 연결
    - 파티션 오프셋 위치 기록
    - Consumer 그룹을 통해 병렬 처리
  - group.id 설정
  - subscribe 메소드 통해 특정 토픽의 모든 파티션 구독, assign을 일부 파티션 구독
  - key가 존재하면 데이터의 순서 보장 가능
  - 컨슈머 API의 핵심은 브로커로부터 연속적으로, 컨슈머가 허락하는 한 많은 데이터 읽는 것
  - poll 메소드 인자 시간동안 데이터 기다림
  - __consumer_offsets에 어디까지 읽었는 지 저장 : 컨슈머 그룹별, 토픽별로 offsets 나누어 저장
