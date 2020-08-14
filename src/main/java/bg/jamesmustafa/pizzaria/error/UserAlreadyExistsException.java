package bg.jamesmustafa.pizzaria.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Email already exists.")
public class UserAlreadyExistsException extends RuntimeException{
    protected int statusCode;

    public UserAlreadyExistsException() {
        this.statusCode = 400;
    }

    public UserAlreadyExistsException(String message) {
        super(message);
        this.statusCode = 400;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
