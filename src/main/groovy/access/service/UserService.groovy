package access.service


import access.exception.UsernameNotFoundException
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@CompileStatic
@Service
class UserService {

    private final access.model.persistence.UserRepository userRepository
    private final AuthService authService

    @Autowired
    UserService(access.model.persistence.UserRepository userRepository, AuthService authService) {
        this.userRepository = userRepository
        this.authService = authService
    }

    String obtainUsername(String token) {
        Map<String,Object> claims = authService.extractTokenClaims(token)
        claims["email"]
    }

    access.model.entities.User findUser(String username) {
        userRepository.findById(username)
                .orElseThrow({ new UsernameNotFoundException("No user found with username " + username)})
    }

}