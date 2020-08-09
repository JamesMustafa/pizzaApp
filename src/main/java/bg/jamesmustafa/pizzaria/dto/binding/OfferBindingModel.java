package bg.jamesmustafa.pizzaria.dto.binding;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

//TODO: Can I add more validation annotations ?
public class OfferBindingModel {

    private Long id;

    @NotBlank(message = "Price cannot be blank")
    @DecimalMin(value = "0.00", inclusive = false)
    private BigDecimal promoPrice;

    @Future(message = "Please select a date in the future")
    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE)
    private LocalDateTime validUntil;

    @NotEmpty(message = "There must be more than zero products")
    private List<String> products;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public BigDecimal getPromoPrice() {
        return promoPrice;
    }

    public void setPromoPrice(BigDecimal promoPrice) {
        this.promoPrice = promoPrice;
    }

    public LocalDateTime getValidUntil() { return validUntil; }

    public void setValidUntil(LocalDateTime validUntil) { this.validUntil = validUntil; }

    public List<String> getProducts() {
        return products;
    }

    public void setProducts(List<String> products) {
        this.products = products;
    }
}
