package bg.jamesmustafa.pizzaria.service;

import bg.jamesmustafa.pizzaria.dto.binding.OfferBindingModel;
import bg.jamesmustafa.pizzaria.dto.binding.OrderBindingModel;
import bg.jamesmustafa.pizzaria.dto.binding.ProductBindingModel;
import bg.jamesmustafa.pizzaria.dto.binding.auth.UserServiceModel;
import bg.jamesmustafa.pizzaria.dto.view.CartProductViewModel;
import bg.jamesmustafa.pizzaria.dto.view.ProductDetailsViewModel;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class CartService {

    private final ModelMapper modelMapper;
    private final UserDetailsServiceImpl userDetailsService;
    private final OfferService offerService;

    public CartService(HttpSession httpSession, ModelMapper modelMapper, UserDetailsServiceImpl userDetailsService, OfferService offerService) {
        this.modelMapper = modelMapper;
        this.userDetailsService = userDetailsService;
        this.offerService = offerService;
    }

    public void initializeCart(HttpSession session) {
        if (session.getAttribute("shopping-cart") == null) {
            session.setAttribute("shopping-cart", new LinkedList<>());
        }
    }

    public List<CartProductViewModel> retrieveCart(HttpSession session) {
        this.initializeCart(session);
        return (List<CartProductViewModel>) session.getAttribute("shopping-cart");
    }

    public void addOneProductToCart(ProductBindingModel productDTO, HttpSession session){
        this.initializeCart(session);
        ProductDetailsViewModel product = this.modelMapper
                .map(productDTO, ProductDetailsViewModel.class);
        CartProductViewModel cartProductViewModel = new CartProductViewModel();
        cartProductViewModel.setProductDetailsViewModel(product);
        cartProductViewModel.setQuantity(1); //one because there is counter in addItemToCart method and it will collect all the same products
        var cart = this.retrieveCart(session);
        this.addItemToCartView(cartProductViewModel, cart);
    }

    public void addOneProductToCart(ProductBindingModel productDTO, int quantity , HttpSession session){
        this.initializeCart(session);
        ProductDetailsViewModel product = this.modelMapper
                .map(productDTO, ProductDetailsViewModel.class);
        CartProductViewModel cartProductViewModel = new CartProductViewModel();
        cartProductViewModel.setProductDetailsViewModel(product);
        cartProductViewModel.setQuantity(quantity); //one because there is counter in addItemToCart method and it will collect all the same products
        var cart = this.retrieveCart(session);
        this.addItemToCartView(cartProductViewModel, cart);
    }

    public void addListOfProductsToCart(List<ProductBindingModel> products, HttpSession session){
        for (ProductBindingModel productDTO : products){
            addOneProductToCart(productDTO,session);
        }
    }

    public void addItemToCartView(CartProductViewModel cartProductViewModel, List<CartProductViewModel> cartProductViewModelList) {
        for (CartProductViewModel item : cartProductViewModelList) {
            if (item.getProductDetailsViewModel().getId().equals(cartProductViewModel.getProductDetailsViewModel().getId())) {
                item.setQuantity(item.getQuantity() + cartProductViewModel.getQuantity());
                return;
            }
        }
        cartProductViewModelList.add(cartProductViewModel);
    }

    public BigDecimal calcTotal(List<CartProductViewModel> cart) {
        BigDecimal result = new BigDecimal(0);
        for (CartProductViewModel item : cart) {
            result = result.add(item.getProductDetailsViewModel().getPrice().multiply(new BigDecimal(item.getQuantity())));
        }
        return result;
    }

    public void removeItemFromCart(Long id, List<CartProductViewModel> cart) {
        cart.removeIf(c -> c.getProductDetailsViewModel().getId().equals(id));
    }

    public OrderBindingModel prepareOrder(List<CartProductViewModel> cart, String customer, String comment, BigDecimal price) {
        OrderBindingModel orderBindingModel = this.mapToOrder(customer,comment,price);
        List<ProductBindingModel> products = new ArrayList<>();
        for (CartProductViewModel item : cart) {
            ProductBindingModel productDTO = this.modelMapper.map(item.getProductDetailsViewModel(), ProductBindingModel.class);

            for (int i = 0; i < item.getQuantity(); i++) {
                products.add(productDTO);
            }
        }
        orderBindingModel.setProducts(products);
        return orderBindingModel;
    }

    //should check here if i remove the price from constructor and just add it from the offer ;)
    public OrderBindingModel prepareOrderFromOffer(Long offerId, String comment, BigDecimal price, String customer){
        OrderBindingModel orderBindingModel = this.mapToOrder(customer,comment,price);
        OfferBindingModel offer = this.offerService.findById(offerId);
        orderBindingModel.setProducts(offer.getProducts());
        return orderBindingModel;
    }

    //I think that this should not be here.
    public boolean checkIfEmailConfirmed(String username){
        return this.userDetailsService.loadUserByUsername(username).getEmailConfirmed();
    }

    private OrderBindingModel mapToOrder(String customer, String comment, BigDecimal price){
        OrderBindingModel orderBindingModel = new OrderBindingModel();
        orderBindingModel.setComment(comment);
        orderBindingModel.setTotalPrice(price);
        orderBindingModel.setCustomer(this.modelMapper.map(this.userDetailsService.loadUserByUsername(customer), UserServiceModel.class));
        return orderBindingModel;
    }
}
