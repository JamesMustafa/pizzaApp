package bg.jamesmustafa.pizzaria.web.controller;

import bg.jamesmustafa.pizzaria.dto.binding.OfferBindingModel;
import bg.jamesmustafa.pizzaria.dto.binding.OrderBindingModel;
import bg.jamesmustafa.pizzaria.dto.binding.ProductBindingModel;
import bg.jamesmustafa.pizzaria.dto.binding.auth.UserServiceModel;
import bg.jamesmustafa.pizzaria.dto.view.CartProductViewModel;
import bg.jamesmustafa.pizzaria.dto.view.ProductDetailsViewModel;
import bg.jamesmustafa.pizzaria.service.OfferService;
import bg.jamesmustafa.pizzaria.service.OrderService;
import bg.jamesmustafa.pizzaria.service.ProductService;
import bg.jamesmustafa.pizzaria.service.UserDetailsServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("/cart")
@PreAuthorize("hasRole('CUSTOMER')")
public class CartController {

    private final ProductService productService;
    private final OfferService offerService;
    private final OrderService orderService;
    private final ModelMapper modelMapper;
    private final UserDetailsServiceImpl userDetailsService;

    public CartController(ProductService productService, OfferService offerService, OrderService orderService, ModelMapper modelMapper, UserDetailsServiceImpl userDetailsService) {
        this.productService = productService;
        this.offerService = offerService;
        this.orderService = orderService;
        this.modelMapper = modelMapper;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/addProduct")
    public String addToCartConfirm(@ModelAttribute(name="productId") Long productId, int quantity, HttpSession session) {
        this.initCart(session);
        ProductDetailsViewModel product = this.modelMapper
                .map(this.productService.findById(productId), ProductDetailsViewModel.class);

        CartProductViewModel cartProductViewModel = new CartProductViewModel();

        cartProductViewModel.setProductDetailsViewModel(product);
        cartProductViewModel.setQuantity(quantity);

        var cart = this.retrieveCart(session);
        this.addItemToCart(cartProductViewModel, cart);

        return "redirect:/products/";
    }

    @PostMapping("/reOrder")
    public String reOrderConfirm(@ModelAttribute(name="orderId") Long orderId, HttpSession session) {

        OrderBindingModel orderBindingModel = this.orderService.findById(orderId);
        addProductsToCart(orderBindingModel.getProducts(), session);
        return "redirect:/cart/details";
    }

    @PostMapping("/addOffer")
    public String addOfferConfirm(@ModelAttribute(name="offerId") Long offerId, HttpSession session) {

        OfferBindingModel offerBindingModel = this.offerService.findById(offerId);
        addProductsToCart(offerBindingModel.getProducts(), session);
        return "redirect:/cart/details";
    }

    private void addProductsToCart(List<ProductBindingModel> products, HttpSession session){
        for (ProductBindingModel productDTO : products){

            this.initCart(session);
            ProductDetailsViewModel product = this.modelMapper
                    .map(productDTO, ProductDetailsViewModel.class);

            CartProductViewModel cartProductViewModel = new CartProductViewModel();
            cartProductViewModel.setProductDetailsViewModel(product);
            cartProductViewModel.setQuantity(1); //one because there is counter in addItemToCart method and it will collect all the same products


            var cart = this.retrieveCart(session);
            this.addItemToCart(cartProductViewModel, cart);
        }
    }

    @GetMapping("/details")
    public String cartDetails(Model model, HttpSession session) {

        var cart = this.retrieveCart(session);
        model.addAttribute("totalPrice", this.calcTotal(cart));

        return "cart/details";
    }

    //TODO: DeleteMapping and method="delete" are not working, but when it's Post everything is fine. Why is that?
    @PostMapping("/removeProduct")
    public String removeFromCartConfirm(@ModelAttribute(name="deleteId") Long deleteId, HttpSession session) {
        this.removeItemFromCart(deleteId, this.retrieveCart(session));

        return "redirect:/cart/details";
    }

    @PostMapping("/checkout")
    public String checkoutConfirm(String comment, HttpSession session, Principal principal) {

        var cart = this.retrieveCart(session);

        OrderBindingModel orderBindingModel = this.prepareOrder(cart, principal.getName(), comment);
        this.orderService.addOrderForApproval(orderBindingModel);
        session.removeAttribute("shopping-cart");

        return "redirect:/home";
    }


    private void initCart(HttpSession session) {
        if (session.getAttribute("shopping-cart") == null) {
            session.setAttribute("shopping-cart", new LinkedList<>());
        }
    }


    private void addItemToCart(CartProductViewModel cartProductViewModel, List<CartProductViewModel> cartProductViewModelList) {
        for (CartProductViewModel item : cartProductViewModelList) {
            if (item.getProductDetailsViewModel().getId().equals(cartProductViewModel.getProductDetailsViewModel().getId())) {
                item.setQuantity(item.getQuantity() + cartProductViewModel.getQuantity());
                return;
            }
        }

        cartProductViewModelList.add(cartProductViewModel);
    }

    private List<CartProductViewModel> retrieveCart(HttpSession session) {
        this.initCart(session);

        return (List<CartProductViewModel>) session.getAttribute("shopping-cart");
    }

    private BigDecimal calcTotal(List<CartProductViewModel> cart) {
        BigDecimal result = new BigDecimal(0);
        for (CartProductViewModel item : cart) {
            result = result.add(item.getProductDetailsViewModel().getPrice().multiply(new BigDecimal(item.getQuantity())));
        }

        return result;
    }

    private void removeItemFromCart(Long id, List<CartProductViewModel> cart) {
        cart.removeIf(c -> c.getProductDetailsViewModel().getId().equals(id));
    }

    private OrderBindingModel prepareOrder(List<CartProductViewModel> cart, String customer, String comment) {
        //TODO: Should I use service models in controller
        OrderBindingModel orderBindingModel = new OrderBindingModel();
        orderBindingModel.setComment(comment);
        orderBindingModel.setCustomer(this.modelMapper.map(this.userDetailsService.findUserByUsername(customer), UserServiceModel.class));
        List<ProductBindingModel> products = new ArrayList<>();
        for (CartProductViewModel item : cart) {
            ProductBindingModel productDTO = this.modelMapper.map(item.getProductDetailsViewModel(), ProductBindingModel.class);

            for (int i = 0; i < item.getQuantity(); i++) {
                products.add(productDTO);
            }
        }

        orderBindingModel.setProducts(products);
        orderBindingModel.setTotalPrice(this.calcTotal(cart));

        return orderBindingModel;
    }

}
