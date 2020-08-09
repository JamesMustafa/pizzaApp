package bg.jamesmustafa.pizzaria.db.entity;

import bg.jamesmustafa.pizzaria.db.entity.common.BaseEntity;

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
    private Boolean successful; //thymeleaf does not recognize it if it's isSuccessful

    @Column(name = "waiting_time")
    private LocalDateTime waitingTime;

    @Column(name = "is_approved", nullable = false)
    private Boolean isApproved;

    @Column(name = "comment")
    private String comment;

    @ManyToMany(targetEntity = Product.class, fetch = FetchType.LAZY)
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

    @ManyToOne(targetEntity = User.class)
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

    public Boolean getSuccessful() { return successful; }

    public void setSuccessful(Boolean successful) { this.successful = successful; }

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

    public String getComment() { return comment; }

    public void setComment(String comment) { this.comment = comment; }
}
