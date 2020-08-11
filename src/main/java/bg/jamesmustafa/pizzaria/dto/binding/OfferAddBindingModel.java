package bg.jamesmustafa.pizzaria.dto.binding;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OfferAddBindingModel {

    @NotBlank(message = "Price cannot be blank")
    @DecimalMin(value = "0.00", inclusive = false)
    private BigDecimal promoPrice;

    @NotBlank(message = "Date cannot be blank")
    private String validUntil;

    @NotEmpty(message = "There must be more than zero products")
    private String[] products; // It is string[] because multiple select option in html works with it.

    public BigDecimal getPromoPrice() {
        return promoPrice;
    }

    public void setPromoPrice(BigDecimal promoPrice) {
        this.promoPrice = promoPrice;
    }

    public String getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(String validUntil) {
        this.validUntil = validUntil;
    }

    public String[] getProducts() {
        return products;
    }

    public void setProducts(String[] products) {
        this.products = products;
    }
}
