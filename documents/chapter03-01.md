## create back-end scaffold with spring-boot
---
back-end 를 위한 java Web Application 은 spring initializr 를 이용해서 프로젝트의 뼈대를 구성한다.   
[spring initializer](https://start.spring.io/) 사이트에 접속해서 spring-boot App 의 기본 뼈대를 구성 한다.   
> Project : Gradle - Groovy   
> Language : Kotlin   
> Spring Boot : 3.2.1
>       
> Project Metadata   
>> Group : com.open   
>> Artifact : taskagile   
>> Name : taskagile   
>> Description : spring boot web project for taskagile   
>> package : jar     
>> java : 21    
   
> Dependencies :   
>> Spring Boot DevTools   
>> Spring Reactive Web   
>> Spring Data R2DBC   
>> H2 Database   
>> MariaDB Driver   

위와 같이 기본 구성을 설정하고 프로젝트를 Generate 하고 back-end 폴더에 구성한다.   
   
먼저 .editorconfig 파일을 구성해서 모든 팀원의 에디터에서 동일한 탭 사이즈를 사용하도록 한다.   
필수적인 설정은 아니지만 가능하면 코드 파일의 가독성을 동일하게 유지하기 위해서 가능하면 먼저 구성 한다.   
   
build.gradle 을 열고 h2database 의 설정을 testImplementation 으로 변경한다.   
h2 는 unit 테스트 만을 위한 로컬 메모리 DB 이며 실제 배포에는 배포 되지 않도록 구성한다.
``` gradle
// before
  runtimeOnly 'com.h2database:h2'
  runtimeOnly 'io.r2dbc:r2dbc-h2'
// after
  testImplementation 'com.h2database:h2'
  testImplementation 'io.r2dbc:r2dbc-h2'
```
build.gradle 의 추가적인 의존성은 프로젝트를 진행 하면서 추가하도록 한다.
   
application.properties 는 application.yml 로 변경한다.   
application.yml 의 세부적인 구성은 코드를 만들어 가면서 구체화 한다.
   
실제로 생성된 코드 구성은 chapter03 brnach 의 내용을 참고 하도록 한다.
