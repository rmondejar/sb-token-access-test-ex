package access.controller

import access.model.entities.User
import access.service.AuthService
import access.service.UserService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import spock.lang.Shared
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

@ActiveProfiles("test")
class AuthControllerUnitSpec extends Specification {

    @Shared ObjectMapper mapper
    @Shared MockMvc mockMvc
    @Shared AuthService authService
    @Shared Map<String,String> loginParams

    void setupSpec() {

        Properties secrets = new Properties()
        File propertiesFile = new File("./src/main/resources/secrets.properties")
        propertiesFile.withInputStream { is -> secrets.load(is) }
        String sharedSecret = secrets.get("JWT_SECRET")

        this.loginParams = ["username":"user.test@gmail.com"]

        authService = new AuthService(sharedSecret)
        User user = new User(username:loginParams["username"], role:"admin")

        UserService userService = Mock(UserService)
        userService.findUser(loginParams["username"]) >> user

        AuthController authController = new AuthController(authService, userService, 6000)
        this.mockMvc =  MockMvcBuilders.standaloneSetup(authController).build()

        this.mapper = new ObjectMapper()

    }

    def "check /auth/token fails because the user not exist"() {
        given:
        Map<String,String> wrongParams = ["username":"nobody@gmail.com"]
        ResultActions result = this.mockMvc.perform(post("/auth/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(wrongParams)))

        when:
        MockHttpServletResponse response = result.andReturn().response

        then:
        response.status == 404
    }

    def "check /auth/token returns the expected content"() {
        given:
        ResultActions result = this.mockMvc.perform(post("/auth/token")
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(loginParams)))

        when:
        MockHttpServletResponse response = result.andReturn().response

        then:
        response.status == 200
        response.contentType
        response.contentAsString
        response.contentAsString.length() > 0
    }

    def "validate /auth/token returned token and its claims"() {
        given:
        ResultActions result = this.mockMvc.perform(post("/auth/token", loginParams)
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(loginParams)))
        String token = result.andReturn().response.contentAsString

        when:
        Map<String,Object> claims = authService.extractTokenClaims(token)

        then:
        claims
        claims["typ"] == "authtoken"
        claims["role"] == "admin"
        claims["email"] == loginParams["username"]
    }
}