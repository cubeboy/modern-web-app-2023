# Register Repository Adapter
---
이 프로젝트에서 가장 어려운 부분은 SQL 서버와 연계하는 Repository 구현이다.  
기존에 사용하던 MyBatis, JPA 등은 DBMS 와의 통신이 Blocking 되기 때문에 Async/WebFlux 모델에 적합하지 않다.  
물론 우회해서 사용할 수 있는 방법이 없는 것은 아니지만 WebFlux 를 사용해서 얻을 수 있는 장점들을 상당부분 잠식하기 때문에 권장되는 방법이 아니다.   
SQL DBMS 의 비동기 실행을 지원하는 라이브러리가 R2DBC 이다.   
```gradle
  implementation 'org.springframework.boot:spring-boot-starter-data-r2dbc'
```
R2DBC 는 JPA 와 비슷한 R2dbcRepository Interface 를 제공한다.   
하지만 동적인 쿼리를 지원하는 기능들은 아직 많이 부족한 상태라서 실무에서 사용하기에는 아직도 많이 부족하다.   
이를 보완하기 위해서 r2dbc 를 지원하는 QueryDSL 사용하기로 한다.   
아래의 의존성 정보와 설정을 build.gradle 에 추가 한다.
```gradle
buildscript {
  ext {
    infobipVersion = "9.0.2"
  }
}
// ... 생략
dependencies {
  // ... 생략
	runtimeOnly 'org.mariadb:r2dbc-mariadb:1.1.3'
	runtimeOnly 'org.mariadb.jdbc:mariadb-java-client'
  testImplementation 'com.h2database:h2'
  testImplementation 'io.r2dbc:r2dbc-h2'
  testImplementation 'io.mockk:mockk:1.13.8'

  implementation "com.infobip:infobip-spring-data-r2dbc-querydsl-boot-starter:${infobipVersion}"
  kapt "com.infobip:infobip-spring-data-jdbc-annotation-processor-common:${infobipVersion}"
}

def querydslDir = "$buildDir/generated/querydsl"
sourceSets {
  main.java.srcDirs += [ querydslDir ]
}

tasks.withType(JavaCompile) {
  options.generatedSourceOutputDirectory = file(querydslDir)
}

clean.doLast {
  file(querydslDir).deleteDir()
}
```
Junit 테스트에서는 h2db driver 를, 런타임 실행 시에는 mariadb driver 를 사용하도록 구성 한다.   
먼저 테스트를 위한 configuration 을 추가 한다.
```yaml
# test/resources/application.yml
spring:
  r2dbc:
    url: r2dbc:h2:mem:///testdb

logging:
  level:
    org:
      springframework:
        r2dbc: DEBUG
```
```kotlin
@TestConfiguration
@ComponentScan(basePackages = ["com.open.taskagile.infra.repository.mariadb"])
class TestMariadbConfig {
  @Bean
  fun sqlTemplates(): SQLTemplates {
    return MySQLTemplates()
  }

  @Bean
  fun initializer(connectionFactory: ConnectionFactory?): ConnectionFactoryInitializer {
    val initializer = ConnectionFactoryInitializer()
    initializer.setConnectionFactory(connectionFactory!!)
    val populator = CompositeDatabasePopulator()
    populator.addPopulators(ResourceDatabasePopulator(ClassPathResource("user/initRepository.sql")))
    initializer.setDatabasePopulator(populator)
    return initializer
  }

  @Bean
  fun reactiveTransactionManager(connectionFactory: ConnectionFactory): ReactiveTransactionManager {
    return R2dbcTransactionManager(connectionFactory)
  }
}
```
이제 구현 코드와 테스트 코드를 추가한다.   
```kotlin
@DataR2dbcTest
@EnableQuerydslR2dbcRepositories
@ContextConfiguration(classes = [TestMariadbConfig::class])
@ActiveProfiles("test")
class MariaUserRepositoryTests {
  @Autowired
  lateinit var userRepository: MariaUserRepository

  @Test
  fun `username 으로 user 찾기 성공`() {
    val user = userRepository.findByUsernameOrEmailAddress("username1", "any-email-address")
    user.test()
      .expectSubscription()
      .expectNextCount(1)
      .verifyComplete()
  }

  @Test
  fun `emailAddress 으로 user 찾기 성공`() {
    val user = userRepository.findByUsernameOrEmailAddress("anyUsername", "username2@taskagile.com")
    user.test()
      .expectSubscription()
      .assertNext {
        it.username == "username2" && it.emailAddress == "username2@taskagile.com"
      }
      .verifyComplete()
  }

  @Test
  fun `등록되지 않은 username & emailAddress`() {
    val user = userRepository.findByUsernameOrEmailAddress("anyUsername", "ayn-email-address")
    user.test()
      .expectSubscription()
      .expectNextCount(0)
      .verifyComplete()
  }

  @Test
  fun `정상 등록 후 user_id 반환`() {
    userRepository.register("testuser", "testuser@taskagiel.com", "P@ssword123")
      .test()
      .expectSubscription()
      .assertNext {
        it == 4L
      }
      .verifyComplete()
  }
}
```
구체적인 코드 구현은 branch chapter05 의 구현 코드를 참조 한다.   
r2dbc는 아직 기술의 성숙도가 JPA/MyBatis 에 비교해서 높지 않은 상태이다.   
앞으로 계속해서 추가 해야할 내용이 많이 있을 것이다.   
