# Spring Security Dependency

## Spring Security Dependency
Spring Security 와 jwt java library 를 build.gradle 에 추가 한다.   
```groovy
  implementation "io.jsonwebtoken:jjwt-api:$jjwtVersion"
  runtimeOnly "io.jsonwebtoken:jjwt-impl:$jjwtVersion"
  runtimeOnly "io.jsonwebtoken:jjwt-jackson:$jjwtVersion"
```
추가 하면서 아래 구문을 함께 설정해서 유닛 테스트에서 사용할 임시 암호 키를 자동으로 발급 하도록 구성 한다.
```groovy
test {
  doFirst {
    systemProperty('SECRET_KEY', UUID.randomUUID().toString())
  }
}
```
application.yml 에도 아래와 같이 추가 해서 시스템 파라미터를 사용 할 수 있도록 구성 한다.
### applicaiton.yml
```yml
jwt:
  secretKey: ${SECRET_KEY}
  validityInMs: 36000
```
   
## Docker 배포
SECRET_KEY 변수는 이후에 docker 파라미터로 사용 할 수 있으며 아래와 같이 구성 할 수 있다.   
### bash command
```bash
docker run -e SECRET_KEY=secret_key_here -p 8080:8080 your_image_name
```
### docker-compose.yml 
```yml
services:
  your-service:
    image: your_image_name
    ports:
      - "8080:8080"
    environment:
      - SECRET_KEY=secret_key_here
```
