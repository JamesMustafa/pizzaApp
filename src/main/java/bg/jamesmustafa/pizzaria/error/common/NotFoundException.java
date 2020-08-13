package bg.jamesmustafa.pizzaria.error.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Object not found!")
public abstract class NotFoundException extends RuntimeException{
    protected int statusCode;

    public NotFoundException() {
        this.statusCode = 404;
    }

    public NotFoundException(String message) {
        super(message);
        this.statusCode = 404;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
