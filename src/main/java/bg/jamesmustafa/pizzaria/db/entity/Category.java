package bg.jamesmustafa.pizzaria.db.entity;

import bg.jamesmustafa.pizzaria.db.entity.common.BaseEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "category", targetEntity = Product.class,
            fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Product> products;

    public Category() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
