## sasl 인증 컨슈머 메시지 소비
- 컨슈머로 네이버페이 외부 카프카 브로커에 sasl plain 인증하여 접속해 첫 메시지부터 수신하는 방법입니다.
[참조](https://hahahia.tistory.com/157)

1. build.gradle에 ```implementation 'org.springframework.kafka:spring-kafka'``` // 이거 추가
2. application.properties 에 다음 추가
```
kafka.bootstrap-servers=localhost:9092
kafka.username=username
kafka.password=qwer!@#$
kafka.enable.auto.commit=false
kafka.ackmode=MANUAL
kafka.offset.reset=latest
```
  - ackMode 에 자세한 내용은 아래 링크를 참고하는 것이 좋다. 지금 예제에서는 명시적으로 Ack 를 수행해야 하기 때문에 MANUAL 로 설정
  - [About Committing offsets](https://docs.spring.io/spring-kafka/reference/html/#committing-offsets)
3. MyKafkaConsumerProperties.java
```java
package com.example.hahahia.config;

import com.example.hahahia.service.MyAcknowledgingMessageListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

@Configuration
@EnableKafka
public class MyKafkaConsumerConfig {
    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.username}")
    private String userName;

    @Value("${kafka.password}")
    private String password;

    @Value("${kafka.offset.reset}")
    private String offsetResetMode;

    @Value("${kafka.ackmode}")
    private ContainerProperties.AckMode ackMode;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        /**
         * https://docs.spring.io/spring-kafka/reference/html/#committing-offsets
         * */
        factory.getContainerProperties().setAckMode(ackMode);

        /**
         * consumer 를 처리하는 Thread 개수(consumer 개수를 의미하는 것은 아님)
         * */
        factory.setConcurrency(3);
        /**
         * 명시적으로 Ack 처리를 위해 별도의 AcknowledgingMessageListener 구현
         * https://docs.spring.io/spring-kafka/reference/html/#message-listeners
         * */
        factory.getContainerProperties().setMessageListener(myAcknowledgingMessageListener());
        factory.getContainerProperties().setPollTimeout(5000);

        return factory;
    }

    public MyAcknowledgingMessageListener myAcknowledgingMessageListener() {
        return new MyAcknowledgingMessageListener();
    }

    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(MyKafkaConsumerProperties.getProperties(bootstrapServers, userName, password));
    }
}
```
  - JAAS File 을 따로 import 하지 않고 JAAS Template 를 String Format 으로 관리한다. username/password 만 파라미터로 받아 지정하도록 구현하였다.
    - JAAS는 클라이언트 및 서버 시스템을 보호하기 위한 유연하고 확장성 있는 자바 인증 및 권한 부여 서비스를 제공하는 표준 API 입니다.
    - J2SE 1.4 부터 표준 확장 API(javax.security.auth)로 배포
    
4. MyKafkaConsumerConfig.java
```java
package com.example.hahahia.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class MyAcknowledgingMessageListener implements AcknowledgingMessageListener<String, String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyAcknowledgingMessageListener.class);

    @Override
    @KafkaListener(topics = "test", groupId = "test-group-1234", containerFactory = "kafkaListenerContainerFactory")
    public void onMessage(ConsumerRecord data, Acknowledgment acknowledgment) {
        try {
            LOGGER.info("consume data: " + data.toString());
            acknowledgment.acknowledge();
        } catch (Exception e) {
            LOGGER.error("consume cause exception : " + e);
        }
    }

}
```
  - 해당 예제에서는 명시적 Ack를 수행해주어야 하기 때문에 ConcurrentKafkaListenerFactory Bean을 생성할 때, setMessageListener 를 별도로 해주었다.
  - Spring Kafka 에서는 AckMode 를 사용하기 위해서는 AcknowledgingMessageListener interface 를 구현해야 한다. 자세한 내용은 message listener 와 위에서 언급하였던 commit offset 모드에 대한 튜토리얼을 참고
  - https://docs.spring.io/spring-kafka/reference/html/#message-listeners
