package bg.jamesmustafa.pizzaria.data.models.view;

import bg.jamesmustafa.pizzaria.data.models.service.UserServiceModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDetailsViewModel {

    private Long id;
    private BigDecimal totalPrice;
    private UserServiceModel customer;
    private LocalDateTime createdOn;
    private Boolean successful; //thymeleaf does not recognize it if it's isSuccessful
    private LocalDateTime waitingTime;
    private String comment;
    private List<ProductDetailsViewModel> products;
    private Long waitingMinutes;

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

    public Boolean getSuccessful() {
        return successful;
    }

    public void setSuccessful(Boolean successful) {
        this.successful = successful;
    }

    public LocalDateTime getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(LocalDateTime waitingTime) {
        this.waitingTime = waitingTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<ProductDetailsViewModel> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDetailsViewModel> products) {
        this.products = products;
    }

    public Long getWaitingMinutes() {
        return waitingMinutes;
    }

    public void setWaitingMinutes(Long waitingMinutes) {
        this.waitingMinutes = waitingMinutes;
    }
}
