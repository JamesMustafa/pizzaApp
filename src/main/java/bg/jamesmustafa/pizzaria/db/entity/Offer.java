package bg.jamesmustafa.pizzaria.db.entity;

import bg.jamesmustafa.pizzaria.db.entity.common.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "offers")
public class Offer extends BaseEntity {

    @Column(name = "old_price", nullable = false)
    private BigDecimal oldPrice;

    @Column(name = "promotional_price", nullable = false)
    private BigDecimal promoPrice;

    @Column(name = "valid_until", nullable = false)
    private LocalDateTime validUntil;
    //TODO: Learn about all the cascadetypes...
    @ManyToMany(targetEntity = Product.class, cascade = CascadeType.MERGE) //with cascadetype.all it gives exception
    @JoinTable(
            name = "offers_products",
            joinColumns = @JoinColumn(
                    name = "offer_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "product_id",
                    referencedColumnName = "id"
            )
    )
    private List<Product> products;


    public Offer() {
    }

    public BigDecimal getOldPrice() { return oldPrice; }

    public void setOldPrice(BigDecimal oldPrice) { this.oldPrice = oldPrice; }

    public BigDecimal getPromoPrice() {
        return promoPrice;
    }

    public void setPromoPrice(BigDecimal promoPrice) {
        this.promoPrice = promoPrice;
    }

    public LocalDateTime getValidUntil() { return validUntil; }

    public void setValidUntil(LocalDateTime validUntil) { this.validUntil = validUntil; }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
