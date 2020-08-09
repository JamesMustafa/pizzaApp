package bg.jamesmustafa.pizzaria.dto.view;

import bg.jamesmustafa.pizzaria.dto.binding.auth.UserServiceModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderHistoryViewModel {

    private Long id;
    private BigDecimal totalPrice;
    private UserServiceModel customer;
    private LocalDateTime createdOn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public UserServiceModel getCustomer() {
        return customer;
    }

    public void setCustomer(UserServiceModel customer) {
        this.customer = customer;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

}
