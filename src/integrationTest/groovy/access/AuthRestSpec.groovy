package access

import io.micronaut.http.HttpHeaders
import io.micronaut.http.client.exceptions.HttpClientException
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.exceptions.HttpClientResponseException

import access.model.api.UserDataResponse

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = Application )
class AuthRestSpec extends Specification {

    @Shared
    @LocalServerPort
    Long serverPort

    @Shared
    @AutoCleanup
    HttpClient client

    void setupSpec() {
        String baseUrl = "http://localhost:${serverPort?:8080}"
        this.client  = HttpClient.create(new URL(baseUrl))
    }

    def "Check token authentication when everything goes wrong"() {

        when: 'Accessing a secured URL without authenticating'
        client.toBlocking().exchange(HttpRequest.GET('/user/data'))

        then: 'returns bad request (missing header)'
        HttpClientResponseException e = thrown(HttpClientResponseException)
        e.status == HttpStatus.BAD_REQUEST

        when: 'Accessing a secured URL with a wrong token'
        client.toBlocking().exchange(HttpRequest.GET('/user/data').header(HttpHeaders.AUTHORIZATION, "####################"))

        then: 'returns unauthorized'
        HttpClientException e2 = thrown(HttpClientResponseException)
        e2.response.status == HttpStatus.UNAUTHORIZED

        when: 'Accessing a secured URL with an old token'
        client.toBlocking().exchange(HttpRequest.GET('/user/data')
                .header(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE1NTY3OTY3ODJ9.hXF3Ak54Sq4Yz07Gl9ELTvHkOY_hiHaPA4fga9GHNQXO_ovCRp7KIwa2WKCOKyfz7zSt0iZEArw4hiZMNslxyg"))

        then: 'returns unauthorized'
        HttpClientException e3 = thrown(HttpClientResponseException)
        e3.response.status == HttpStatus.UNAUTHORIZED
    }

    def "Verify token authentication actually works"() {

        when: 'Login endpoint is called with valid credentials'
        Map loginParams = ["username":"user.test@gmail.com"]
        HttpRequest request = HttpRequest.POST('/auth/token', loginParams)
        HttpResponse<String> loginResponse = client.toBlocking().exchange(request, String)

        then: 'the endpoint can be accessed'
        notThrown HttpClientResponseException
        loginResponse
        loginResponse.status == HttpStatus.OK
        loginResponse.body()
        loginResponse.body().size() > 0

        when:
        String token = loginResponse.body()
        HttpRequest requestWithAuthorization = HttpRequest.GET('/user/data' ).header(HttpHeaders.AUTHORIZATION, "Bearer $token")
        HttpResponse<UserDataResponse> userResponse = client.toBlocking().exchange(requestWithAuthorization, String)

        then:
        notThrown HttpClientResponseException
        userResponse
        userResponse.status == HttpStatus.OK
        userResponse.body()
        userResponse.body().toString().contains(loginParams["username"])
    }
}