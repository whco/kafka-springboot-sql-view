https://victorydntmd.tistory.com/348 참조
- Springboot에서 Kafka의 특정 Topic에 메시지를 생산(Produce)하고 해당 Topic을 Listen함.
- Kafka 서버에 해당 메시지가 전달되고, Springboot에서 이를 소비(Consume)할 준비가 되면 메시지를 pull 하는 간단한 예제
> Kafka 개념은 연동 구성 후 학습예정
  - 카프카에서 토픽은 파티션(partition)이라고 하는 단위로 나누어지며, 클러스터의 각 서버들에 분산되어 저장
  - 복제(replication)는 고가용성을 위해 설정하는데, 데이터를 각 서버들(즉, 다른 파티션들)에 저장을 해놓고, 
    장애가 발생했을때 복제해놓은 데이터를 이용해서 fail over을 하게 된다. 이때 이 fail over는 파티션 단위로 이루어진다.

## 환경 세팅
- 프로젝트 구조 세팅
- Kafka 설치 및 실행(참조 : [참조1](https://developer-youngjun.tistory.com/13), [참조2](https://alphahackerhan.tistory.com/11))
  - 카프카를 실행하려면 주키퍼를 먼저 실행해야 한다. 그 후 카프카 서버 실행.
    - 주키퍼가 무엇? : Kafka의 컨트롤러선정, 브로커 메타데이터 저장, 토픽 메타데이터 저장, 
      client 할당 정보 저장, 카프카 토픽 ACL(Access Control Lists) 저장 등의 상당히 중요한 역할
  ```linux
  bin/zookeeper-server-start.sh config/zookeeper.properties #주키퍼 서버 실행
  netstat -an | grep 2181 #확인
  bin/kafka-server-start.sh config/server.properties #카프카 서버 실행(새 창)
  bin/kafka-topics.sh -create -zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test #토픽 추가(새 창)
  bin/kafka-topics.sh --list --zookeeper localhost:2181 #토픽 리스트 확인
  bin/kafka-topics.sh --delete --zookeeper localhost:2181 --topic test #생성된 토픽 삭제
  bin/kafka-console-producer.sh --bootstrap-server localhost:9092 --topic test #Producer 실행 및 메세지 전송
  bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test --from-beginning #Consumer 실행(새 창)
  #test라는 토픽에 해당하는 데이터가 들어오면 받겠다
  ```
  - 

- build.gradle에 implementation 'org.springframework.kafka:spring-kafka' 추가

## 구현

- application.yaml
  - yml 과 yaml은 동일. 
  ```yaml
  spring:
  kafka:
    consumer:
      bootstrap-servers: localhost:9092
      group-id: foo
      auto-offset-reset: earliest
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
    producer:
      bootstrap-servers: localhost:9092
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
  
  ```
  
  
  
- KafkaController.java
```java
import com.victolee.kafkaexam.Service.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/kafka")
public class KafkaController {
    private final KafkaProducer producer;

    @Autowired
    KafkaController(KafkaProducer producer) {
        this.producer = producer;
    }

    @PostMapping
    public String sendMessage(@RequestParam("message") String message) {
        this.producer.sendMessage(message);

        return "success";
    }
}
```

- KafkaProducer.java

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {
    private static final String TOPIC = "exam";
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaProducer(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String message) {
        System.out.println(String.format("Produce message : %s", message));
        this.kafkaTemplate.send(TOPIC, message);
    }
}
```


- KafkaConsumer.java
```java
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class KafkaConsumer {

    @KafkaListener(topics = "exam", groupId = "foo")
    public void consume(String message) throws IOException {
        System.out.println(String.format("Consumed message : %s", message));
    }
}
```
