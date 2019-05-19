package access.service


import groovy.transform.CompileStatic
import io.jsonwebtoken.SignatureAlgorithm

import java.time.Instant
import java.time.temporal.ChronoUnit

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtBuilder
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@CompileStatic
@Service
class AuthService {

    private final String tokenSecret
    private final SignatureAlgorithm signatureAlgorithm


    AuthService (@Value('${token.secret}') String tokenSecret) {
        this.tokenSecret = tokenSecret
        this.signatureAlgorithm = SignatureAlgorithm.HS512;
    }

    String generateToken(access.model.entities.User user, int expiresIn) {

        Map<String, Object> claims = createClaims(user)

        JwtBuilder jwt = Jwts.builder()
                .setHeaderParam("alg", "HS512")
                .setHeaderParam("typ", "JWT")
                .setId(UUID.randomUUID().toString())
                .setExpiration(Date.from(Instant.now().plus(expiresIn, ChronoUnit.SECONDS)))
                .setNotBefore(Date.from(Instant.ofEpochSecond(0)))
                .setIssuedAt(Date.from(Instant.now()))
                .setIssuer("https://token.access.ex")
                .setAudience("public")
                .setSubject("showcase")
                .addClaims(claims)

        sign(jwt)

        jwt.compact()
    }

    Claims extractTokenClaims(String token) {

        if (token.startsWith("Bearer ")) {
            token = token.substring("Bearer ".length());
        }

        Map<String, Object> claims = Jwts.parser()
                .setSigningKey(tokenSecret.bytes)
                .parseClaimsJws(token).body

        claims
    }

    protected JwtBuilder sign(JwtBuilder jwt) {

        jwt.signWith(signatureAlgorithm, tokenSecret.bytes)

        jwt
    }

    protected Map<String, Object> createClaims(access.model.entities.User user) {

        Map<String, Object> claims = new HashMap<>()
        claims["typ"] = "authtoken"
        claims["role"] = user.role
        claims["name"] = "${user.name} ${user.surname}"
        claims["given_name"] = user.name
        claims["family_name"] = user.surname
        claims["email"] = user.username

        claims
    }
}