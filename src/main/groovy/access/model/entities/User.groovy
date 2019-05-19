package access.model.entities

import groovy.transform.CompileStatic

import javax.persistence.Entity;
import javax.persistence.Id;

@CompileStatic
@Entity
class User {

    @Id
    private String username
    private String role
    private String name
    private String surname

    String getUsername() {
        return username
    }

    String getRole() {
        return role
    }

    String getName() {
        return name
    }

    String getSurname() {
        return surname
    }

}
