package access.controller


import access.model.api.UserDataResponse
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CompileStatic
@RestController
class UserController {


    private final access.service.UserService userService

    private static final Logger logger = LoggerFactory.getLogger(UserController)

    @Autowired
    UserController(access.service.UserService userService) {
        this.userService = userService
    }

    @RequestMapping(path = "/user/data", produces = [ "application/json" ], method = RequestMethod.GET)
    ResponseEntity<UserDataResponse> getUserData(@RequestHeader(value = "Authorization") String authToken) {

        String username
        try {
            username = userService.obtainUsername(authToken)
        } catch(e) {
            logger.error(e.message)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED)
        }

        if (!username) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY)
        }

        access.model.entities.User user = userService.findUser(username)
        if (!user) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND)
        }

        UserDataResponse response = new UserDataResponse(user)

        ResponseEntity.ok(response)
    }
}