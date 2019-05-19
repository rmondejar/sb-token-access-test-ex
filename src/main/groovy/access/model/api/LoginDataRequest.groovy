package access.model.api

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.CompileStatic

@CompileStatic
class LoginDataRequest {

    @JsonProperty("username")
    String username
}
