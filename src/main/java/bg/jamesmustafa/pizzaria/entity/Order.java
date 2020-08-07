package bg.jamesmustafa.pizzaria.entity;

import bg.jamesmustafa.pizzaria.entity.common.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "is_successful")
    private Boolean isSuccessful;

    @Column(name = "waiting_time")
    private LocalDateTime waitingTime;

    @Column(name = "is_approved", nullable = false)
    private Boolean isApproved;

    @ManyToMany(targetEntity = Product.class, fetch = FetchType.EAGER)
    @JoinTable(
            name = "orders_products",
            joinColumns = @JoinColumn(
                    name = "order_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "product_id",
                    referencedColumnName = "id"
            )
    )
    private List<Product> products;

    @ManyToOne(optional = true, targetEntity = User.class)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User customer;

    public Order() {
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Boolean getSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(Boolean successful) {
        isSuccessful = successful;
    }

    public LocalDateTime getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(LocalDateTime waitingTime) {
        this.waitingTime = waitingTime;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public User getCustomer() { return customer; }

    public void setCustomer(User customer) { this.customer = customer; }

    public Boolean getApproved() { return isApproved; }

    public void setApproved(Boolean approved) { isApproved = approved; }
}
