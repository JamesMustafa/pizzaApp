package bg.jamesmustafa.pizzaria.dto.view;

import java.math.BigDecimal;
import java.util.List;

public class CartViewModel {

    private List<CartProductViewModel> products;
    private BigDecimal totalPrice;

    public List<CartProductViewModel> getProducts() {
        return products;
    }

    public void setProducts(List<CartProductViewModel> products) {
        this.products = products;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
