package bg.jamesmustafa.pizzaria.error;

import bg.jamesmustafa.pizzaria.error.common.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Offer not found!")
public class OfferNotFoundException extends NotFoundException {

    public OfferNotFoundException(String message) {
        super(message);
    }

    public int getStatusCode() {
        return statusCode;
    }
}
