package access.service

import access.model.entities.User
import io.jsonwebtoken.security.WeakKeyException

import java.time.Instant

import org.springframework.test.context.ActiveProfiles
import spock.lang.Shared
import spock.lang.Specification

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtBuilder
import io.jsonwebtoken.Jwts

@ActiveProfiles("test")
class AuthServiceUnitSpec extends Specification {

    @Shared
    AuthService authService

    @Shared
    User user

    void setupSpec() {

        Properties secrets = new Properties()
        File propertiesFile = new File("./src/main/resources/secrets.properties")
        propertiesFile.withInputStream { is -> secrets.load(is) }
        String sharedSecret = secrets.get("JWT_SECRET")

        authService = new AuthService(sharedSecret)
        user = new User(username:"test.user@gmail.com", role:"admin")
    }

    void "Verify we can sign with the shared key"() {

        given:
        JwtBuilder jwt = Jwts.builder().setClaims(["iss":"test"])

        when:
        jwt = authService.sign(jwt)
        String token = jwt.compact()

        then:
        Jwts.parser().isSigned(token)
        notThrown WeakKeyException
    }

    void "Check signed and expired token"() {

        given:
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE1NTY3OTY3ODJ9.hXF3Ak54Sq4Yz07Gl9ELTvHkOY_hiHaPA4fga9GHNQXO_ovCRp7KIwa2WKCOKyfz7zSt0iZEArw4hiZMNslxyg"

        when:
        authService.extractTokenClaims(token)

        then:
        thrown ExpiredJwtException
        Jwts.parser().isSigned(token)
    }

    void "Verify signature from a token"() {

        given:
        String token

        when:
        token = authService.generateToken(user, 36000)

        then:
        Jwts.parser().isSigned(token)

    }

    void "Wait and check expiration time of a few seconds"() {

        given:
        int expirationTime = 2
        String token = authService.generateToken(user,expirationTime)

        when:
        Claims claims = authService.extractTokenClaims(token)

        then:
        claims
        claims.size() > 0
        claims.getExpiration() > Date.from(Instant.now())
        Jwts.parser().isSigned(token)

        when:
        Thread.sleep(expirationTime*1000)
        authService.extractTokenClaims(token)

        then:
        claims.getExpiration() < Date.from(Instant.now())
        thrown ExpiredJwtException

    }

    void "Verify claims parsing a token"() {

        given:
        String token = authService.generateToken(user, 36000)

        when:
        Claims claims = authService.extractTokenClaims(token)

        then:
        Jwts.parser().isSigned(token)
        claims.getExpiration()
        claims.getExpiration() > Date.from(Instant.now())
        claims.getNotBefore()
        claims.getIssuedAt()
        claims.getId()
        claims.getAudience()
        claims.get("email") == user.username
    }
}