package access.model.api


import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.CompileStatic

@CompileStatic
class UserDataResponse {

    @JsonProperty("username")
    String username
    @JsonProperty("role")
    String role
    @JsonProperty("name")
    String name
    @JsonProperty("surname")
    String surname

    UserDataResponse(access.model.entities.User user) {

        this.username = user.username
        this.role = user.role
        this.name = user.name
        this.surname = user.surname
    }

}
