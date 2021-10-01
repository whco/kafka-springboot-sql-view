## 7가지 ackMode 변경해 보고 장단점 분석
컨슈머의 poll() 메서드는 하나 혹은 그 이상의 ConsumerRecords를 반환<br>
MessageListener는 각 레코드를 위해 호출

- RECORD : 레코드 단위로 프로세싱 이후 커밋
  - 레코드가 처리된 후, 리스너가 리턴할 때 오프셋 커밋

- BATCH : poll() 메소드로 호출된 레코드 모두 처리 이후 반환 시 커밋

- TIME : 특정 시간 이후 커밋

- COUNT : 특정 개수만큼 레코드 처리 후 커밋

> 위 4가지 옵션을 적용해 보았으나 로그 상으로 차이점을 명확히 인지하기 어려웠다.

- COUNT_TIME : TIME, COUNT 중 하나라도 만족 시 커밋

- MANUAL : Acknowledgement.acknowleddge() 호출 시 다음 poll() 때 커밋. AcknowledgingMessageListner 또는 BatchAcknowledgingMessageListner 사용해야 함

- MANUAL_IMMEDIATE : Acknowledgement.acknowleddge() 호출 즉시 커밋. AcknowledgingMessageListner 또는 BatchAcknowledgingMessageListner 사용해야 함

스프링 카프카데드 레터 재처리 방식
- SeekToCurrentErrorHandler를 통해서 메시지 처리 재시도, 최대 실패수 도달 시 Recoverer 동작 설정 가능
- DeadLetterPublishingRecoverer : 실패한 메시지 다른 토픽으로 보냄
- 다른 토픽으로 보낸 이후는??
