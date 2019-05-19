package access.exception

import groovy.transform.CompileStatic

@CompileStatic
class UsernameNotFoundException extends Exception {
    UsernameNotFoundException(String message) {
        super(message)
    }
}
