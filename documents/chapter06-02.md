# JWT Token Generate
---
먼저 JWT 구성 정보 관리 객체를 구현한다.

### JwtPropertiesTests.kt
```kotlin
  @Test
  fun appStartInitJwtProperties() {
    val secretKey = System.getProperty("SECRET_KEY") ?: ""
    log.info("generated secretKey :: {}", secretKey)
    Assertions.assertEquals(secretKey, jwtProperties.secretKey)
    Assertions.assertEquals(36000L, jwtProperties.validityInMs)
  }
```
앞서 application.yml 에 추가 했던 구성 정보가 잘 등록되고 작동 하는지 확인 하는 테스트 코드 이다.   
시스템 환경 변수에 등록된 값이 application.yml 값에 바인딩 되는지 검증 한다.   
그런데 다른 테스트에서 오류가 발생 할 것이다.   
**RegisterControllerTests** 에 등록된 테스트 들이 모두 403 오류를 발생 시킨다.
Spring Security 를 적용하면 기본 적으로 로그인 인증 되지 않은 모든 URL 요청들이 거부된다.   
따라서 인증이 필요하지 않은 URL 에 대한 구성을 추가 해야 한다.   
아래 구현을 추가해서 인증이 필요하지 않은 요청에 대해서 예외를 추가한다.   

### SecurityConfiguration.kt
```kotlin
@Configuration
@EnableWebFluxSecurity
class SecurityConfiguration {
  companion object {
    val PUBLIC = arrayOf("/", "/error", "/login", "/api/registrations")
  }

  @Bean
  fun springWebFilterChain(http:ServerHttpSecurity): SecurityWebFilterChain {
    return http
      .authorizeExchange { exchange ->
        exchange
          .pathMatchers(*PUBLIC).permitAll()
          .anyExchange().permitAll()
      }
      .csrf{ it.disable() }
      .build()
  }
}
```

### RegisterControllerTests.kt
```kotlin
@ExtendWith(MockKExtension::class)
@WebFluxTest(RegisterController::class)
@Import(SecurityConfiguration::class)
class RegisterControllerTests {
  ...
}
```
이제 command 라인에서 테스트를 실행 하더라도 모든 테스트가 통과 된다.
```bash
gradlew test
```

## Token Generate
JWT Token 생성과 파싱은 **JwtTokenProviderTests** 의 테스트 구현을 참조한다.   
JwtTokenProvider 는 토큰 생성과 파싱 그리고 토큰의 유효성 검사를 책임진다.   
UsernamePasswordAuthenticationToken 에 대한 구현만 제공 하도록 구현 하지만 이후에 부가적인 사용자 정보를 제공 하도록 확장 하는 구현을 추가 할 예정이다.   
