# Register Web Service Adapter
---
## 웹 컨트롤러 (어뎁터)
웹 컨트롤러는 RegisterPage(front-end)와의 통신 프로토콜을 제어 한다.   
> RegisterPage 에서 전달 하는 데이터의 유효성 검사   
> RegisterPage 의 데이터를 서비스 포트의 규격에 맞게 변경
> Service Port 를 이용해서 서비스 호출
> RegistePage 가 요구하는 규격에 맞게 변경하여 반환

### 5.1 RegisterPage 에서 전달 하는 데이터의 유효성 검사   
RegisterPage 가 전달하는 데이터를 객체화 하고 객체 값의 유효성 검사를 구현 한다.   
이런 역할을 Payload 로 정의하고 RegisterPayload 를 구현 한다.   
RegisterPayload 는 RegisterPage 와 동일한 속성과 속성에 대한 유효성 검사를 제공해야 한다.   
> username 은 최소 4 글자 이상, 최대 20자 이하 이며 필수 입력이다.   
> emailAddress 는 최소 6 글자 이상, 최대 100자 이하 이며 필수입력이다.   
> emailAddress [계정@도메인] 형태의 email format 을 지켜야 한다.   
> password 는 최소 6자 이상 최대 128자 이하이며 필수입력이다.   
> password 는 영문자 1개 이상 과 숫자 1개 이상이 반드시 포함되어야 한다.   

이번에는 RegisterPayload 가 제공해야하는 유효성 검사에 대한 테스트 요구사항을 먼저 구현 하고 유효성 검사 기능을 추가 하는 순서로 진행 하자.   
유효성 검사는 spring boot validation 을 사용한다.   
아래 의존성을 build.gradle 에 추가 한다.
```gradle
// spring boot 2.3 version 이전 버전은 spring-boot-starter-web 에 포함 
implementation 'org.springframework.boot:spring-boot-starter-validation'
```
   
RegisterPayload 클래스를 만든다.   
```kotlin
// main com.open.taskagile.web.payload.RegistrationPayload
class RegistrationPayload(
  val username:String,
  val emailAddress:String,
  val password:String
)
```
   
테스트를 위한 junit 클래스 RegistrationPayloadTests 를 만든다.
```kotlin
// test com.open.taskagile.web.payload.RegistrationPayloadTest
package com.open.taskagile.web.payload

import jakarta.validation.Validation
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class RegistrationPayloadTests {
  private val validator = Validation.buildDefaultValidatorFactory().validator
  private val username = "testuser"
  private val emailAddress = "testuser@taskagile.com"
  private val password = "P@asword12"

  @Test
  fun `username 은 필수입력, 최소 4글자 이상, 최대 20자 이하`() {
val shortUsername = RegistrationPayload("sht", emailAddress, password)
    val shortViolations = validator.validate(shortUsername).toList()
    assertEquals(REGISTER_USERNAME_LENGTH, shortViolations[0].message)
  }
}
```
테스트를 실행 하기위해 테스트를 실행 한다.   
아래 예제는 gradle 명령어를 이용하는 방법이며 eclipse, intelli J 를 이용한 테스트 방법은 별도로 기술 하지 않는다.   
```bash
./gradlew test
# ... 생략
RegistrationPayloadTests > username 은 필수입력, 최소 4글자 이상, 최대 20자 이하() FAILED
    org.opentest4j.AssertionFailedError at RegistrationPayloadTests.kt:18
```
FAILED 메시지를 확인 할 수 있다.   
이제 테스트를 통과 할 수 있는 코드를 만든다.   
```kotlin
class RegistrationPayload(
  @field:Pattern(regexp="^[a-zA-Z0-9]{4,20}$",
    message = REGISTER_USERNAME_INVALID)
  val username:String,

  @field:Pattern(regexp = "^(?=.{6,100}$)[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
    message = REGISTER_EMAIL_ADDRESS_INVALID )
  val emailAddress:String,

  @field:Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()-_=+\\[\\]{}|;:'\",.<>?/])[A-Za-z\\d!@#$%^&*()-_=+\\[\\]{}|;:'\",.<>?/]{6,128}$",
    message = REGISTER_PASSWORD_INVALID)
  val password:String
)
```
이제 테스트를 다시 실행하면 테스트를 통확 하고 BUILD SUCCESSFUL 메시지를 학인 할 수 있다.   
나머지 필드의 유효성 검사는 chapter05 branch 의 코드를 참조 한다.   

**mockk 를 이용한 컨트롤러 테스트**   
payload 의 유효성이 WebController 와의 통신에서 잘 작동하늕 확인 하기위한 Test Unit 을 만들기 전에 mockk lib 를 추가 한다.   
mockk 는 mockito 를 kotlin 에서 대체하는 mocking lib 이다.   
Web Controller 테스트를 위한 준비작업으로 아래 의존성을 build.gradle 에 추가한다.   
```gradle
testImplementation 'io.mockk:mockk:1.13.8'
```
mockk 에 대한 자세한 정보는 공식 폼페이지를 확인 한다.   
[mocking library for kotlin](https://mockk.io/)   
Web Controller 구현과 테스트 코드는 chapter05 branch 내용을 참조 한다.   
   
### 5.2 RegisterPage 의 데이터를 서비스 포트의 규격에 맞게 변경

### 5.3 Service Port 를 이용해서 서비스 호출
### 5.4 RegistePage 가 요구하는 규격에 맞게 변경하여 반환
