# Register Core Service
---
Rgister Core Service 는 크게 두가지 기능을 제공한다.   
> Repository 에 User 등록하기   
> 가입자에게 가입 완료 알림 전달하기   
> 가입완료 이벤트 로깅하기   
   
Repository 저장은 먼저 username/emailAddress 가 등록되어 있는지 먼저 확인하고 저장을 진행한다.   
먼저 앞서 만드었던 RegistrationService 를 상속받는 구현 클래스를 만들고 이 클래스를 테스트 하는 코드를 먼저 만든다.   
   
테스트 코드를 먼저 만드는 것은 단순히 구현을 단계가 아니다.   
테스트 코드를 만들면서 코드의 구현 결과를 상상이 아닌 물리적은 구현으로 직접 설계하는 과정을 가지는 것이다.   
이런 과정을 거치면서 설계 단계에서의 부족한 구현단계에서 미리 검증 해 볼수 있다.   
테스트 코드의 구현이 힘들고 거추장 스럽게 느껴질 수 있겠지만 테스트는 설계 단계의 연장으로 생각하고 진행 해야 한다.   

테스트 코드에서 먼저 구현해야 할 것은 usrname/emailAddress 가 이미 등록된 정보인지 확인 하는 것이다.
   
```kotlin
  @Test
  fun `중복된 Username 등록 오류`() {
    val command = RegistrationCommand("existsUsername", emailAddress, password)
    every { userRepository.findByUsernameOrEmailAddress(command.username, command.emailAddress) } returns
      User(command.username, "otherUser@taskagile.com").toMono()
    val mono = userService.register(command)
    mono.test()
      .expectSubscription()
      .expectErrorMatches {
        it is RegistrationException &&
          it.message.equals(REGISTER_USERNAME_EXISTS)
      }
      .verify()

    verify(exactly = 0) { userRepository.register(any(), any(), any()) }
    verify(exactly = 0) { eventPublisher.publish(any()) }
    verify(exactly = 0) { userNotifier.notify(any(), UserNotificationType.USER_REGISTERED) }
  }
```
이 테스트를 통과 하기 위해서 아래와 같은 구현 코드를 작성한다.   
```kotlin
  override fun register(command: RegistrationCommand): Mono<Long> {
    return command.toMono()
      .flatMap {
        userRepository.findByUsernameOrEmailAddress(command.username, command.emailAddress)
      }
      .flatMap {
        if(it.username.equals(command.username)) {
          Mono.error<Long>(RegistrationException(REGISTER_USERNAME_EXISTS))
        } else {
          Mono.error<Long>(RegistrationException(REGISTER_EMAIL_ADDRESS_EXISTS))
        }
      }
      .switchIfEmpty {
        userRepository.register(command.username, command.emailAddress, command.password)
      }
  }
```
구현코드는 항상 한가지 정답만을 가지고있지 않고 여러가지 방법으로 구현 할 수 있다.   
현재의 구현이 맞는 것이라도 이후에 얼마든지 다른 방법으로 변경 되 수도 있다.   
하지만 입력과 출력 그리고 오류처리에 대한 요구사항은 유지 된다. 요구사항이 변경 되는 경우에도 검증코드와 구현코드를 함께 변경하면서 하나를 고치면 두개의 오류가 발생하는 사고를 예방할 수 있으며 견고한 구조와 코드를 유지 할 수 있다.    
