# Integration & Test
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
   
---
## R2DBC 구현 변경
초기 단계에서는 infobip.querydsl 을 이용해서 구현 하려 하였으나 구현하면서 테스트 하는 과정에서 여러가지 문제가 계속해서 발생해서 querydsl 구현을 제거하고 r2dbc 구현으로 대체 하기로 한다.   
현재 구현된 Repository 구현을 R2dbc 구현으로 변경한다.   구현 코드 내용은 branch chapter05 의 내용을 참조 한다.   
   
---
### e2e Test
이제 back-end 와 front-end 의 구현이 되었고 docker-compose 를 이용한 구현 환경까지 구성 되었으므로 실제 화면을 작동 할 수 있다.
현재 로컬 환경에서 배포되지 않은 상태이기 때문에 back/front-end 간의 정상적인 통신이 가능하게 하려면 간단한 설정을 해야 한다.   
vite.config.js 의 설정에 server proxy 구성을 추가한다.
```javascript
export default defineConfig({
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080'
      }
    }
  }
})
```
이제 변경된 코드를 컴파일하고 실행하면 /register 페이지를 실행하면 회원 가입 후 /login 페이지로 정상적으로 전환되는 기능을 확인 할 수 있다.   
   
여기까지 진행으로 구현단계에서 할 수 있는 단위 테스트는 일정이상 완료되었다.   
조금더 추가해서 화면상의 데이터를 입력하고 테스트하는 과정을 자동화 해 보도록 한다.   
front-end/e2e 폴더에 register.spec.js 파일을 추가하고 정상적으로 화면이 로딩 되는지를 확인하는 테스트를 추가 한다.   
```javascript
test.describe('Register page tests', () => {
  test('should display the logo image', async ({ page }) => {
    await page.goto('/register')
    const logo = await page.locator('div.v-img')
    await expect(logo).toBeVisible()
  });
});
```
front-end 폴더에서 테스트 명령어를 입력하면 자동으로 브라우저가 열렸다 닫히기를 반복하면서 테스트가 자동으로 진행 된다. 회원정보 입력 후 가입 및 중복가입 오류 테스트는 branch chapter05 의 코드 내용을 참조 한다.   
현재는 단순히 등록 페이지의 내용만 확인하지만 이후 페이지간 이동 과정을 구성해서 종합적인 시나리오 테스트로 발전 할 수 있도록 테스트를 계속해서 추가 한다.
   
