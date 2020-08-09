package bg.jamesmustafa.pizzaria.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Role not found!")
public class RoleNotFoundException extends RuntimeException {

    private int statusCode;

    public RoleNotFoundException() {
        this.statusCode = 404;
    }

    public RoleNotFoundException(String message) {
        super(message);
        this.statusCode = 404;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
