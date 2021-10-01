## 아파치 카프카 애플리케이션 프로그래밍

### 빅데이터 파이프라인에서 카프카의 역할

확장성
- 스케일 아웃, 인 과정은 클러스터의 무중단 운영 지원

영속성
- 파일 I/O 성능 향상 위해 페이지 캐시 영역 메모리에 따로 생성해 사용

고가용성
- 카프카 클러스터 : 3개 이상 서버로 운영(권장)
- 리전 단위 장애에도 데이터 안전하게 복제할 수 있는 브로커 옵션 존재
- 브로커 간 데이터 복제 시간 차 데이터 유실 가능성 : min.insync.replicas 옵션으로 제어

데이터 레이크 아키텍처와 카프카의 미래
- 카파 아키텍처 : 모든 데이터를 스피드 레이어에 넣어서 처리
- 모든 종류의 데이터를 스트림 처리

배치/스트림 데이터
- 배치 데이터 : 한정된 기간 단위 데이터, ex) 지난 1분 간 주문한 제품 목록
- 스트림 데이터 : 한정되지 않은 데이터, ex) 웹 사용자의 클릭 로그, 주식 정보, IoT 센서 데이터
  - 시작 데이터와 끝 데이터가 명확히 정해지지 않은 데이터
  
모든 데이터를 로그로 바라보는 것에서 시작
- 로그 : 데이터의 집합
  - 지속적으로 추가 가능, 일정한 번호 부여
  
  
### 카프카 빠르게 시작해 보기
주키퍼 : 분산 코디네이션 서비스 제공하는 오픈소스
- 분산 코디네이션 서비스 : 분산 시스템 내부에 상태 정보 저장
> 분산 코디네이션 서비스 : 분산된 시스템간의 정보를 공유, 클러스터에 있는 서버들의 상태를 체크, 분산된 서버들간에 동기화를 위한 락(lock)을 처리하는 것 등의 문제 해결
- 데이터를 key/value 저장 및 제공하는 서비스
- 브로커 기본 포트 9092, 주키퍼 기본 포트 2181

제공받은 서버(@dev-internship01-npay-ncl)에서 진행
1. 카프카 다운로드 및 압축 해제
2. 브로커 힙 메모리 설정
- 카프카 브로커는 레코드 내용은 페이지 캐시로 시스템 메모리 사용
- 나머지 객체들을 힙 메모리에 저장
3. 주키퍼, 카프카 실행
- ```config/server.properties``` 에서 브로커 설정
4. 로컬에서 브로커와 정상 통신하는 지 확인
- 브로커 정보 요청

Kafka command line tool
- kafka-topics.sh
  - topic 생성 2가지 상황
    - 컨슈머, 프로듀서에서 브로커에 없는 토픽에 대한 데이터 요청
    - 커맨드 라인 툴로 명시적 생성 요청
  - 토픽 상세 조회 : ```--describe```로 파티션, 리더, 복제, ISR 확인 가능
  - 토픽 옵션 수정 : ```kafka-topics.sh 또는 kafka-configs.sh 수정```
- kafka-console-producer.sh
  - key가 null이면 레코드 배치 단위로 라운드 로빈 전송
- kafka-console-consumer.sh
  - --group 으로 그룹 생성
- kafka-consumer-groups.sh
  - --dscribe 에서
    - current-offset : 컨슈머 그룹이 가져간 토픽의 파티션의 가장 최신 오프셋
    - log-end-offset : 컨슈머 그룹의 컨슈머가 커밋한 오프셋
    - consumer-id : client id + uuid, 컨슈머의 토픽 할당 내부적 구분 위한 id
    - host : 컨슈머가 동작하는 host명
    - client-id : 컨슈머가 할당된 id, 지정 or 자동생성
- kafka-verifiable-producer, consumer.sh
  - producer, consumer 자동 테스트
- kafka-delete-records.sh
  - 가장 낮은 오프셋부터 특정 시점 오프셋까지 삭제
  - delete-topic.json : ```{"partitions":[{"topic":"test", "partition": 0, "offset": 10}], "version":1 }```
    - topic, partition, offset 포함
### 카프카 기본 개념 설명
카프카 브로커, 클러스터, 주키퍼
- 하나의 서버에는 한 개의 카프카 브로커 프로세스 실행
- 데이터 안전하게 보관 처리 위해 3대 이상의 브로커 서버를 1개의 클러스터로 묶어서 운영
- 데이터 저장, 전송
  - ```ls /tmp/kafka-logs/hello.kafka-0```에서
    - .index는 메시지 오프셋 인덱싱 정보
    - .timeindex는 메시지에 포함된 timestamp 기준 인덱싱 정보
  - 카프카는 캐시메모리 구현 안 하고 파일시스템에 저장
  - 페이지 캐시 사용해 디스크 입출력 속도 높임
  - 페이지 캐시 : OS에서 파일 입출력 성능 향상을 위해 만들어 놓은 메모리 영역
  - 한 번 읽은 파일의 내용은 메모리의 페이지 캐시 영역에 저장
  - 힙 메모리 사이즈 크게 설정할 필요가 없음
- 데이터 복제, 싱크
  - 팔로워 파티션들은 리더 파티션의 오프셋 확인해 자신과 다른 경우 리더 파티션에서 데이터 가져와서 저장 : 복제
  - 2 이상 복제 개수 궈장
  - 리더 브로커가 다운되면 팔로워 파티션 중 하나가 리더 넘겨 받음.
- 컨트롤러
  - 클러스터의 다수 브로커 중 한 대가 컨트롤러 역할 맡음
  - 다른 브로커들의 상태 체크, 브로커가 클러스터에서 빠지는 경우 해당 브로커에 존재하는 리더 파티션 재분배
> 브로커가 비정상 작동하는 건 어떻게 확인할까?
- 메시지 삭제
  - 컨슈머나 프로듀서가 데이터 삭제 요청 불가, 오직 브로커만 가능
  - 데이터 삭제는 파일 단위 : 로그 세그먼트(log segment)
  - 삭제 말고 압축할 수도
- 컨슈머 오프셋 저장
  - 컨슈머 그룹은 오프셋 커밋
  - '\__consumer_offsets' 토픽에 저장
- 코디네이터 
  - 클러스터의 브로커 중 한 대는 코디네이터 역할 수행
  - 컨슈머 그룹 상태 체크, 파티션을 컨슈머와 매칭되도록 분배
  - 컨슈머가 컨슈머 그룹에서 빠지면 매칭되지 않은 파티션에 정상 작동 컨슈머 할당 : 리밸런스(rebalance)
  - 주키퍼는 카프카의 메타데이터 관리
  - ```bin/zookeeper-shell.sh my-kafka:2181```
    - 동일 환경에서 실행되는 주키퍼에 접속
    - 주키퍼 쉘을 통해 znode 조회하고 수정 가능
      - znode : 주키퍼에서 사용하는 데이터 저장 단위, 트리 구조
        - 2개 이상의 클러스터 구축 시 root znode 한 단계 아래의 znode를 브로커 옵션으로 지정.
        - 각기 다른 하위 znode로 설정된 클러스터는 각 클러스터의 데이터에 영향 미치지 않음.
    - ```get /brokers/ids/0``` 브로커에 대한 정보 : 보안 규칙, jmx port, host 등
    - ```get /controller``` 컨트롤러 정보
    - ```get /brokers/topics``` 카프카에 저장된 토픽들 확인
    - 클러스터로 묶인 브로커들은 동일한 경로의 주키퍼 경로 선언해야 같은 브로커 묶음이 된다
    - 한 개의 주키퍼에 다수의 클러스터 연결 가능 
- 토픽과 파티션
  - 파티션은 병렬처리의 핵심
    - 컨슈머 그룹이 레코드 병렬 처리하도록 매칭
  - 토픽 이름 조건
    - 빈 문자열 X
    - . .. 불가
    - . _ - 가능 . _ 동시에는 경고
    - . _ 서로 바꿨을 때 이름 중복이라면 불가
    - __consumer_offsets, __transaction_state 불가
    - kebab-case, snake_case 추천
    - 환경, 애플리케이션 이름, 데이터 타입 등 포함 권장
    - 이름 변경 지원 X
- 레코드
  - 타임스탬프, 메시지 키, 메시지 값, 오프셋 구성
  - 한 번 적재된 레코드는 수정 불가
  - 타임스탬프는 적재된 시점 유닉스 시간, 프로듀서가 설정도 가능
  - 키는 순서대로 처리, 또는 메시지 값의 종류 나타내기 위해 사용
  - serialize, deserialize 같은 걸로
- 카프카 클라이언트
  - 프로듀서, 컨슈머, 어드민
  - 라이브러리이기 때문에 자체 라이프사이클 가진 프레임워크나 애플리케이션 위에서 구현하고 실행
  - 프로듀서 API
    - ```send()```는 배치 전송
    - ```flush()```는 내부 버퍼에 있는 레코드 배치를 전송
    - ```close()```는 프로듀서 인스턴스 리소스 안전하게 종료
    - 내부적으로 파티셔너, 배치 생성 단계 거침
    - sender에 전송되기 전 accumulator에서 버퍼로 쌓아놓고 발송
    - 배치로 묶어서 전송함으로써 처리량 향상에 큰 도움
    - UniformStickyPartitioner는 어큐뮬레이터에서 배치로 모두 묶일 때까지 기다렸다가 동일 파티션에 전송
    - ```acks```는 all 해야할 듯
    - ```buffer.memory``` : 브로커로 전송할 데이터 배치로 모으기 위해 설정할 버퍼 메모리양. 기본 32MB
    - ```batch.sizse``` : 배치로 전송할 레코드 최대 용량
    - ```linger.ms``` : 배치 전송 전까지 기다리는 최소 시간. 기본 0
    - ```enable.idempotence``` : 멱등성 프로듀서 할 지. 기본 false
    - ```transactional.id``` : 레코드를 트랜잭션 단위로 묶어서 전송할 지. 프로듀서의 트랜잭션 아이디 설정 가능, 설정 시 트랜잭션 프로듀서로 작동. 기본 null
    - Future 객체
      - send() 사용 시 반환
      - RecordMetadata의 비동기 결과 표현 : 정상적으로 브로커에 적재되었는 지
      - get() 메소드 사용 시 결과를 동기적으로 가져옴 ```RecordMetadata rmd = producer.send(record).get();```
      - send()의 결과값은 브로커로부터 응답 기다리고 받은 후 RecordMetadata 인스턴스 반환
    - onCompletion 콜백 메소드 구현하면 비동기 결과 받을 수 있음 ```producer.send(record, new ProducerCallback()```
      - 반환 순서 역전될 수 있음
  - 컨슈머 API
    - poll 인자 Duration : 컨슈머 버퍼에 데이터를 기다리기 위한 타임아웃 간격
    - 한 컨슈머 그룹에서 1개의 파티션은 최대 1개 컨슈머 할당 가능, 1개 컨슈머는 여러 파티션 할당 가능
    - 컨슈머 그룹은 다른 컨슈머 그룹과 격리
    - 컨슈머에 장애 발생하면 파티션 소유권 넘김 : 리밸런싱
      - 컨슈머 추가
      - 컨슈머 제외(장애 발생 시 등)
      - 그룹 조정자(group coordinator)는 리밸런싱 발동하는 역할, 브로커 중 
        - 컨슈머 추가, 삭제 감지
### 오프셋 커밋
기본값 : enable.auto.commit=true
- poll() 메소드가 auto.commit.interval.ms 설정 값 이상 지났을 때 그 시점까지 읽은 레코드 오프셋 커밋
- poll() 호출 이후 리밸런싱 또는 컨슈머 강제종료 발생 시 데이터 중복 또는 유실 가능성 존재
> 기본이 5000 ms 이면 5초나 이후에 커밋한다는 건가? 5초 동안 레코드를 1000개쯤 처리하면 poll() 이후 4초에 끊기면 800개 쯤 다시 받아야 함 -> 중복 데이터 처리<br>
> 커밋하기 전에 리밸런싱이 일어나면 어떻게 되는 걸까? 컨슈머가 삭제되거나 추가돼서 파티션 오너십 재조정 -> 마지막 읽은 오프셋을 어딘가에 저장해 놓지 않는다면 오프셋 정보 소실 -> 또 읽어야 됨<br>
> 의도적인 리밸런싱 시작 시에는 커밋하고 리밸런싱 시작, 예상치 않은 리밸런싱 시 오프셋 정보 날라갈 듯<br>
> 오프셋을 애플리케이션에 저장하는 건 얼마 안 걸릴 것 같은데, 이를 브로커 토픽에 저장하는 시간이 오래 걸리니까 자주 커밋 안하겠지?<br>
> 정확히 poll() 실행 후 auto.commit.interval.ms 후에 커밋하나? poll() 호출 즉시 컨슈머 종료된다면? : 테스트 필요<br>

#### 오프셋 커밋 시 데이터 중복, 유실에 관한 생각
- 중복 : DB에서 확인 가능할 듯, 용인 가능
  - 식별자 부여
  - 전부 다 확인 시 매우 비효율적
    - 얼마나 걸릴까?
    - 기본 키 중복되는 거 금방 확인 가능한 거 보면 의외로 오래 안 걸릴 지도?
  - 오프셋 커밋 최대 개수가 n개라면 최근 n개만 확인
    - 비효율적
    - n개가 들어온 순서가 보장이 되나?
      - 재시도 메시지 있는 경우에 안 될듯
      - 타임스탬프로 확인? 오래 걸릴 듯
  - DB에 중복으로 넣는 거 허용하고 DB에서 뺄 때 확인해도 될 듯
- 유실 : 절대 용인 불가
  - 오프셋을 커밋했는데 실제로는 소비 안 했을 경우가 있나?
    - 자동커밋 시 
      - 레코드 5개 가져오면 설정 값 이후에 5개 커밋하겠지?
      - 3개만 DB에 넣은 후 애플리케이션 종료되면
      - 나머지 2개 소실
    - commitSync() 사용하면

#### auto offset commit 시 고려할 점들
작동 방식 : poll()로 ConsumerRecords 반환, 이후 auto.commit.interval.ms 지나면 그 시점까지 읽은 레코드 오프셋 커밋
- 애플리케이션에 처리한 오프셋 번호 저장할 것으로 예상 : 

- 명시적 오프셋 커밋은 commitSync() 호출
  - poll() 메소드 통해 반환된 레코드 가장 마지막 오프셋 기준으로 커밋
  - 커밋요청하고 정상처리 응답 받기까지 기다림 -> 처리량 이슈
> poll() 이후 commitSync()가 불리기 전에 컨슈머가 종료된다면? 같은 메시지 두 번 컨슘 예상 -> DB나 애플리케이션에서 중복 데이터 체크 필요
> 유실은 없을 듯
- commitAsync()
  - 커밋 요청 전송하고 응답 오기 전까지 데이터 처리 수행
  - 커밋 실패 시 데이터 순서 보장 X, 중복 처리 발생 가능
  - Fetcher 인스턴스가 미리 레코드들을 내부 큐로 가져 옴.
  - poll() 호출 시 큐에 있는 레코드들 반환받아 처리 수행


#### 컨슈머 주요 옵션
- session.timeout.ms : 컨슈머가 브로커와 연결이 끊기는 최대 시간
  - 이 시간 내에 하트비트 전송하지 않으면 브로커는 컨슈머에 이슈가 발생했다고 가정하고 리밸런싱 시작
  - 보통 하트비트 시간 3배. 기본값 10초
- heartbeat.interval.ms : 하트비트 전송하는 시간 간격. 기본값 3초
> 이 옵션들도 중복 및 유실 데이터 관리할 때 중요할 듯
- max.poll.interval.ms : poll() 메소드 호출하는 간격의 최대 시간
  - poll() 메소드 호출 이후 지나치게 시간 경과 시 비정상 판단. 리밸런싱 시작. 기본값 5분
- isolation.level : 트랜잭션 프로듀서가 레코드를 트랜잭션 단위로 보낼 경우 사용
  - read_committed, read_uncommitted. 기본값 read_uncommitted

비동기 오프셋 커밋
- 동기 오프셋 커밋은 커밋 응답 기다리는 동안 데이터 처리 중단
- 비동기는 응답 기다리지 않음

- 마찬가지로 poll()에서 리턴한 가장 마지막 레코드 기준 오프셋 커밋.
- onComplete 메소드 구현해서 커밋 응답 확인

리밸런스 리스너 가진 컨슈머
- poll() 메소드로 반환받은 데이터 모두 처리 전에 리밸런스 발생 시 데이터 중복 처리
- opPartitionAssigned() : 리밸런스 끝난 뒤 파티션 할당 완료되면 호출됨
- onPartitionRevoked() : 리밸런스 시작 직전 호출
  