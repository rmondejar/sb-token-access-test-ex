package access.controller

import access.model.api.LoginDataRequest
import access.service.AuthService
import access.service.UserService
import access.model.entities.User
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CompileStatic
@RestController
class AuthController {

    private final AuthService authService

    private final UserService userService

    private int expiresIn

    private static final Logger logger = LoggerFactory.getLogger(AuthController)

    @Autowired
    AuthController(AuthService authService, UserService userService, @Value('${token.expiresIn}') int expiresIn) {
        this.authService = authService
        this.userService = userService
        this.expiresIn = expiresIn
    }

    @RequestMapping (path = "/auth/token", consumes = [ "application/json" ], method = RequestMethod.POST)
    ResponseEntity<String> loginUser(@RequestBody LoginDataRequest params) {

        User user = userService.findUser(params.username)
        if (!user) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND)
        }

        String token
        try {
            token = authService.generateToken(user, expiresIn)
        } catch(Exception e) {
            logger.error(e.getMessage())
        }

        ResponseEntity.ok(token)
    }
}