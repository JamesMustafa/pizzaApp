package bg.jamesmustafa.pizzaria.data.dto;

//TODO: Is it better to create to DTO's (InputModel and DetailsModel with different properties) or to use one without the propoerties
//TODO: Can I add more validation annotations ?
import bg.jamesmustafa.pizzaria.entity.Product;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class OfferDTO {

    private Long id;

    @NotBlank(message = "Price cannot be blank")
    @DecimalMin(value = "0.00", inclusive = false)
    private BigDecimal promoPrice;

    @Future(message = "Please select a date in the future")
    @DateTimeFormat(iso= DateTimeFormat.ISO.DATE)
    private Date validUntil;

    @NotEmpty(message = "There must be more than zero products")
    private List<String> products;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPromoPrice() {
        return promoPrice;
    }

    public void setPromoPrice(BigDecimal promoPrice) {
        this.promoPrice = promoPrice;
    }

    public Date getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }

    public List<String> getProducts() {
        return products;
    }

    public void setProducts(List<String> products) {
        this.products = products;
    }
}
