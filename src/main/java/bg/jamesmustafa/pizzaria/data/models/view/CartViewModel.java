package bg.jamesmustafa.pizzaria.data.models.view;

public class CartViewModel {

    private ProductDetailsViewModel productDetailsViewModel;
    private int quantity;

    public CartViewModel() {
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
