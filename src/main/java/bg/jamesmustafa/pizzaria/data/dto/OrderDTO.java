package bg.jamesmustafa.pizzaria.data.dto;

import bg.jamesmustafa.pizzaria.data.models.service.UserServiceModel;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {

    //TODO: Can i put PositiveOrZero to the boolean values??

    private Long id;

    @NotBlank(message = "Price cannot be blank")
    @DecimalMin(value = "0.00", inclusive = false)
    private BigDecimal totalPrice;

    @Future(message = "Please select a time in the future")
    @DateTimeFormat(iso= DateTimeFormat.ISO.TIME)
    private LocalDateTime waitingTime;

    @NotNull(message = "Boolean should be either true or false")
    private Boolean isSuccessful;

    @NotEmpty(message = "There must be more than zero products")
    private List<ProductDTO> products;

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

    public Boolean getSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(Boolean successful) {
        isSuccessful = successful;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }

    public UserServiceModel getCustomer() { return customer; }

    public void setCustomer(UserServiceModel customer) { this.customer = customer; }

    public Boolean getApproved() { return isApproved; }

    public void setApproved(Boolean approved) { isApproved = approved; }

    public String getComment() { return comment; }

    public void setComment(String comment) { this.comment = comment; }
}
