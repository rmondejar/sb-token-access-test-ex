package access.model.persistence

import access.model.entities.User
import groovy.transform.CompileStatic
import org.springframework.data.jpa.repository.JpaRepository;

@CompileStatic
interface UserRepository extends JpaRepository<User, String> {

}

