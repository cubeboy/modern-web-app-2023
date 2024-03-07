## Token Web Filer
---
### JwtTokenAuthenticationFilter
Jwt 토큰을 만들고 검증하는 로직이 만들어 졌다면 발급된 토큰을 검증하고 Request Context 에 전달 하는 로직이 필요하다.   
Client 에 발급된 토큰을 이용해서 Server 로 서비스를 요청할 때는 Spring 에서 제공하는 Web filter chain 을 통과하는 과정에서 토큰의 유효성을 검증하고 context 에 인증 정보를 기록하는 기능을 구현 한다.   
기능의 구현은 WebFilter Interface 를 상속하여 구현하고 Security 의 Filter 로 등록 한다.   
구체적인 구현내용은 코드를 참조 한다.   

** SecurityConfiguration **
```kotlin
class SecurityConfiguration {
  companion object {
    val PUBLIC = arrayOf("/", "/error", "/login", "/api/registrations")
  }

  @Bean
  fun springWebFilterChain(http:ServerHttpSecurity, tokenProvider: JwtTokenProvider): SecurityWebFilterChain {
    return http
      .csrf{ it.disable() }
      .httpBasic { it.disable() }
      .authorizeExchange { exchange ->
        exchange
          .pathMatchers(*PUBLIC).permitAll()
          .anyExchange().permitAll()
      }
      .addFilterAt(JwtTokenAuthenticationFilter(tokenProvider), SecurityWebFiltersOrder.HTTP_BASIC)
      .build()
  }
}
```
이렇게 Configuration 을 추가 하고 나서 테스트를 실행 하면 기존에 테스트에 통과하던 코드들이 오류를 발생 시킬 수 있다.   
JwtPropertiesTests, JwtTokenProviderTests 의 경우 Applicatoin Properties 를 활성화 할 수 있는 구성 설정을 추가 한다.   
```kotlin
@ContextConfiguration(classes = [ApplicationConfiguration::class])
@ConfigurationPropertiesScan("com.open.taskagile.configuration.JwtProperties")
```
RegisterControllerTests 의 경우 SecurityConfiguration 이 필요 하지 않은 테스트이기 때문에 해당 설정이 로딩 되지 않도록 구성 설정을 추가 한다.
```kotlin
@WebFluxTest(
  controllers = [RegisterController::class],
  excludeAutoConfiguration = [
    ReactiveUserDetailsServiceAutoConfiguration::class,
    ReactiveSecurityAutoConfiguration::class
  ]
)
```
이렇게 구성  설정을 추가하게 되면 기존 테스트를 모두 통과 하게 된다.
