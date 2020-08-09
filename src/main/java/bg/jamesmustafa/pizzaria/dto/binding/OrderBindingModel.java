package bg.jamesmustafa.pizzaria.dto.binding;

import bg.jamesmustafa.pizzaria.dto.binding.auth.UserServiceModel;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderBindingModel {

    private Long id;

    @NotBlank(message = "Price cannot be blank")
    @DecimalMin(value = "0.00", inclusive = false)
    private BigDecimal totalPrice;

    @Future(message = "Please select a time in the future")
    @DateTimeFormat(iso= DateTimeFormat.ISO.TIME)
    private LocalDateTime waitingTime;

    @NotNull(message = "Boolean should be either true or false")
    private Boolean successful; //thymeleaf does not recognize it if it's isSuccessful

    @NotEmpty(message = "There must be more than zero products")
    private List<ProductBindingModel> products;

    @NotNull(message = "Customer cannot be null")
    private UserServiceModel customer;

    @NotNull(message = "Boolean should be either true or false")
    private Boolean isApproved;

    @Size(max = 200, message = "Comment length must be less than 200 characters")
    private String comment;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getWaitingTime() { return waitingTime; }

    public void setWaitingTime(LocalDateTime waitingTime) {
        this.waitingTime = waitingTime;
    }

    public Boolean getSuccessful() { return successful; }

    public void setSuccessful(Boolean successful) { this.successful = successful; }

    public List<ProductBindingModel> getProducts() { return products; }

    public void setProducts(List<ProductBindingModel> products) { this.products = products; }

    public UserServiceModel getCustomer() { return customer; }

    public void setCustomer(UserServiceModel customer) { this.customer = customer; }

    public Boolean getApproved() { return isApproved; }

    public void setApproved(Boolean approved) { isApproved = approved; }

    public String getComment() { return comment; }

    public void setComment(String comment) { this.comment = comment; }
}
