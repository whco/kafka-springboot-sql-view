## Kafka Connect
[카프카 커넥트](https://always-kimkim.tistory.com/entry/kafka101-connect) 참조
[카프카 커넥트 한글번역](https://godekdls.github.io/Apache%20Kafka/kafka-connect/) 참조
- 클라이언트는 그 외부 시스템에 맞춰 구현되고 관리되어야 합니다. 즉, 개발 비용이 필요합니다. 
카프카 커넥트는 이러한 개발 비용을 없애고, 쉽고 간단하게 메시지 파이프라인을 구성할 수 있도록 도와주는 아파치 카프카 프로젝트 중 하나
- 단일 모드(Standalone)와 분산 모드(Distributed)
- 일반적으로 단일 모드는 개발 테스트 환경에서 사용하고, 분산 모드는 운영 환경에서 사용
- 커넥터(Connector)는 커넥트 내부의 실제 메시지 파이프라인
- 커넥'트'에 커넥'터'를 구성하기 위해선 커넥'트'에 커넥'터' 관련 설정(property) 명세를 전달
- 단일 모드의 경우 파일 형태로, 분산 모드의 경우 REST API를 통해 전달
- 설정 값은 모든 커넥터에 필수인 설정과 커넥터 별 설정
- JDBC 관련된 커넥터를 생성하는 요청이기 때문에 connection에 관련된 설정이 필요합니다. 만약 File sink connector를 구성했다면, 파일 경로 설정
- 커넥터는 주기적으로 메시지를 확인하고, 새로운 메시지가 있으면 파이프라인을 통해 흘려보냅니다.
- 커넥트는 일종의 템플릿이 구현되어 있고, 그 템플릿과 설정 값을 기준으로 인스턴스를 생성
  - 그 템플릿을 플러그인(plugin)이라고 정의

관련 내부 구성 요소
- 커넥터 (Connector) : 파이프라인에 대한 추상 객체. task들을 관리
- 테스크 (Task) : 카프카와의 메시지 복제에 대한 구현체. 실제 파이프라인 동작 요소
- 워커 (Worker) : 커넥터와 테스크를 실행하는 프로세스
- 컨버터 (Converter) : 커넥트와 외부 시스템 간 메시지를 변환하는 객체. 
- 트랜스폼 (Transform) : 커넥터를 통해 흘러가는 각 메시지에 대해 간단한 처리
- 데드 레터 큐 (Dead Letter Queue) : 커넥트가 커넥터의 에러를 처리하는 방식

- 카프카에 데이터는 바이너리 형태기 때문에 알맞은 형식으로 다시 파싱 해야 한다. (schemas)
그리고 프로듀서랑 컨슈머에서 작동하게 하면 되는 거 아닌가 할 수 있지만,

프로듀서랑 컨슈머의 코드가 길어지면 길어질수록 디버깅이랑 관리가 힘들어 질 것이다.
- Kafka Connect plugin is a set of JAR files containing the implementation of one or more connectors, transforms, or converters

### 카프카 커넥트 vs 프로듀서/컨슈머조
참조 : Kafka the definitive guide 책
권장 사용처
- 프로듀서/컨슈머 : 카프카에 연결할 외부 애플리케이션 코드를 변경할 수 있으면서 카프케에 데이터를 쓰거나 읽을 때
- 카프카 커넥터 : 코드 작성하지 않았고 변경할 수 없는 외부 시스템

### Mysql Sink Connector for Confluent Cloud
https://docs.confluent.io/cloud/current/connectors/cc-mysql-sink.html

Features
- Table and column auto-creation: auto.create and auto-evolve are supported. If tables or columns are missing, they can be created automatically. Table names are created based on Kafka topic names.
- Database authentication: username/password authentication.
- Schemas: The connector supports Avro, JSON Schema, and Protobuf input data formats. Schema Registry must be enabled to use a Schema Registry-based format.
- Modes: This connector inserts and upserts Kafka records into a MySQL database.
- Primary key support: Supported PK modes are kafka, none, and record_value. Used in conjunction with the PK Fields property.
Confluent Cloud cluster에 대한 접근권한 필요
1. 인풋 메시지 포맷 선택 : json schema 선택 가능


![스크린샷 2021-09-02 오후 5 13 25](https://media.oss.navercorp.com/user/26171/files/4009bc80-0c11-11ec-96ea-a5745dec1d5a)

![스크린샷 2021-09-02 오후 5 14 00](https://media.oss.navercorp.com/user/26171/files/4bf57e80-0c11-11ec-8ea1-58a4f32b02fc)


쉽고 간편한 커넥션 목적으로 사용하기에 따라가야 할 사항이 너무 많고,
어차피 스키마 설정을 따로 해 줘야 하고
spring-kafka로 카프카 메시지 확인하고 파싱하는 것이 제어가 더 용이해 보임
