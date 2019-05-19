# Spring Boot 2 Spock Testing Example (Token-Based Access)

The main idea of this project is to demonstrate how to take advantages from the most famous tools and Groovy DSLs (Gradle & Spock) to create readable, comprensive, tested and well-maintained Spring Boot 2 Rest Services.

*Disclaimer : this project intention is to create a suitable scenario which enables the implementation of interesting unit and integration tests. Otherwise, the recommendation is to rely on trusted auth & security solutions (Spring Security, Keycloak, ...)*


## Dependencies
* [Apache Groovy](http://groovy-lang.org/)
* [Spring Boot 2](https://spring.io/projects/spring-boot)
* [Spock Framework](https://github.com/spockframework)
* [Gradle Buil Tool](https://gradle.org/)
* [Micronaut Http Client](https://guides.micronaut.io/micronaut-http-client-groovy/guide/index.html#tests)
* [JJWT Library](https://github.com/jwtk/jjwt)


## Lessons Learned 

1. How to implement a Spring Boot 2 application with a gradle plugin [[link](https://github.com/rmondejar/sb-token-access-test-ex/blob/master/build.gradle#L3)]
2. How to create unit mocked tests with Spring Boot 2 and Spock :
* Implementing unit tests for services [[link](https://github.com/rmondejar/sb-token-access-test-ex/blob/master/src/test/groovy/access/service/AuthServiceUnitSpec.groovy)]
* Implementing unit tests for controllers [[link](https://github.com/rmondejar/sb-token-access-test-ex/blob/master/src/test/groovy/access/controller/AuthControllerUnitSpec.groovy)]
3. How to create realistic rest integration tests using the micronaut http client [[link](https://github.com/rmondejar/sb-token-access-test-ex/blob/master/src/integrationTest/groovy/access/AuthRestSpec.groovy)]
4. How to launch integration tests with a gradle plugin [[link](https://github.com/rmondejar/sb-token-access-test-ex/blob/master/build.gradle#L4)]
5. How to externalize secrets from the main application and tests using Spring Boot 2 [[link](https://github.com/rmondejar/sb-token-access-test-ex/blob/master/src/test/groovy/access/service/AuthServiceUnitSpec.groovy)]
6. How to handle JWT tokens using a Java library [[link](https://github.com/rmondejar/sb-token-access-test-ex/blob/master/src/main/groovy/access/service/AuthService.groovy)]

## Command Line

To start the application :
```
./gradlew bootRun
```

To launch unit tests :
```
./gradlew test
```

To launch integration tests :
```
./gradlew integrationTest
```

To launch all the tests :
```
./gradlew check
```

