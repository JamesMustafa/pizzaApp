package bg.jamesmustafa.pizzaria.dto.view;

import java.math.BigDecimal;

public class CartProductViewModel {

    private ProductDetailsViewModel productDetailsViewModel;
    private int quantity;

    public CartProductViewModel() {
    }

    public ProductDetailsViewModel getProductDetailsViewModel() {
        return productDetailsViewModel;
    }

    public void setProductDetailsViewModel(ProductDetailsViewModel productDetailsViewModel) {
        this.productDetailsViewModel = productDetailsViewModel;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
