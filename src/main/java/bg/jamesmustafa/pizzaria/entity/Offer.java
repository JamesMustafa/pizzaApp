package bg.jamesmustafa.pizzaria.entity;

import bg.jamesmustafa.pizzaria.entity.common.BaseDeletableEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "offers")
public class Offer extends BaseDeletableEntity {

    @Column(name = "promotional_price", nullable = false)
    private BigDecimal promoPrice;

    @Column(name = "valid_until", nullable = false)
    private Date validUntil;

    @ManyToMany(targetEntity = Product.class, cascade = CascadeType.ALL)
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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
