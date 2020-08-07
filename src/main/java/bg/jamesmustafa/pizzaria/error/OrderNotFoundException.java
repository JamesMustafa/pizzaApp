package bg.jamesmustafa.pizzaria.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Order not found!")
public class OrderNotFoundException extends RuntimeException {

    private int statusCode;

    public OrderNotFoundException() {
        this.statusCode = 404;
    }

    public OrderNotFoundException(String message) {
        super(message);
        this.statusCode = 404;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
