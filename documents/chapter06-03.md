## Token Web Filer
---
### JwtTokenAuthenticationFilter
Jwt 토큰을 만들고 검증하는 로직이 만들어 졌다면 발급된 토큰을 검증하고 Request Context 에 전달 하는 로직이 필요하다.   
Client 에 발급된 토큰을 이용해서 Server 로 서비스를 요청할 때는 Spring 에서 제공하는 Web filter chain 을 통과하는 과정에서 토큰의 유효성을 검증하고 context 에 인증 정보를 기록하는 기능을 구현 한다.   
기능의 구현은 WebFilter Interface 를 상속하여 구현하고 Security 의 Filter 로 등록 한다.   
구체적인 구현내용은 코드를 참조 한다.   

### Security Configuration

