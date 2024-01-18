Integration & Test
---
지금까지 작성된 코드는 단위 테스트로서는 잘 작동하는 상태이다.   
하지만 Interface 만 존재하고 구현체가 없기때문에 Application 을 실행 했을때 오류가 발생 한다.   
현재 구현 되지 않은 Interface는 EventPublisher/UserNotifier 이다.   

- EventPublisher : 회원 가입이 발생 할 때마다 시스템 내부적으로 처리 해야 할 프로세스들이 있다. 회원가입 이력등록 및 이후 발생 할 수 있는 여러가지 처리 과정들은 회원가입 과정과의 결합도를 낮추기 위해서 SpringFramework 에서 제공하는 ApplicationEventPublisher 를 이용해서 처리 하도록 구현 한다. 현재는 UserRegisteredEventPublisher 구현에 단순 로그만 기록 하도록 하고 이후 구현에서 ElasticSearch 를 이용해서 회원가입 이벤트 로깅 처리를 추가 하도록 한다.
- UserNotifier : 회원 가입이 완료된 후에는 회원에게 Email 또는 SNS 서비스를 이용해서 회원 가입에 대한 알림을 처리 할 필요가 있다. 현재는 EmailUserNotifier 를 구현해서 단순 로그만 기록 하도록 하고 이후 구현에서 RabbitMQ or Kafka 를 이용해서 비동기 처리로 메일을 발송 할 수 있도록 구현 하도록 한다.
   
추가적인 작업으로 MariadbConfig 를 추가 해서 R2dbcRepository 를 활성화 하고 사용 할 수 있는 구성 설정을 추가한다.   
자세한 구현 코드는 branch capter05 의 구현 코드를 참조 하도록 한다.
이렇게 구성하고 back-end 폴더에서 ./gradlew test, ./gradlew bootRun 명령어를 실행하면 테스트 결과와 정상적인 실행 결과를 확인 할 수 있다.   
실제로 Appication 의 정상 실행 상태를 확인하기 위해 build.gradle 에 아래 라이브러리를 추가하자.   
```gradle
  implementation 'org.springframework.boot:spring-boot-starter-actuator'
```
이제 bootRun 을 실행하고 웹브라우저에서 http://localhost:8080/actuator/health 에 접속하면 app 의 상태를 확인 할 수 있다.   
```json
{
    "status": "UP"
}
```
actuator 는 이후에 prometheus 를 이용한 모니터링 구성 단게에서 자세하게 설명한다.   

