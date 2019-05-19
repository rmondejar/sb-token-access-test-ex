# Spring Boot 2 Spock Testing Example (Token-Based Access)

Disclaimer : this project intention is to create an interesting scenario to enable unit and integration tests, otherwise is wiser to use reliable auth & security solutions

## Dependencies : 
* Groovy
* Spring Boot 2 
* Spock Framework
* Gradle Buil Tool
* Micronaut Http Client

## CLI

To start the application
```
./gradlew bootRun
```

To launch unit tests
```
./gradlew test
```

To launch integration tests
```
./gradlew integrationTest
```

To launch all the tests
```
./gradlew check
```

## Lessons Learned 

1. How to create unit mocked tests with Spring Boot 2 and Spock
* Implementing unit tests for services [[link](https://github.com/rmondejar/sb-token-access-test-ex/blob/master/src/test/groovy/access/service/AuthServiceUnitSpec.groovy)]
* Implementing unit tests for controllers [[link](https://github.com/rmondejar/sb-token-access-test-ex/blob/master/src/test/groovy/access/controller/AuthControllerUnitSpec.groovy)]
2. How to create realistic rest integration tests using the micronaut http client [[link](https://github.com/rmondejar/sb-token-access-test-ex/blob/master/src/integrationTest/groovy/access/AuthRestSpec.groovy)]
3. How to launch integration tests with gradle [[link](https://github.com/rmondejar/sb-token-access-test-ex/blob/master/build.gradle#L4)]
4. How to externalize secrets from the main application and tests using Spring Boot 2 [[link](https://github.com/rmondejar/sb-token-access-test-ex/blob/master/src/test/groovy/access/service/AuthServiceUnitSpec.groovy)]
