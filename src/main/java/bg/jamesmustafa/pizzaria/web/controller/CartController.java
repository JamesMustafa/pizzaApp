package bg.jamesmustafa.pizzaria.web.controller;

import bg.jamesmustafa.pizzaria.data.models.view.CartViewModel;
import bg.jamesmustafa.pizzaria.data.models.view.ProductDetailsViewModel;
import bg.jamesmustafa.pizzaria.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final ProductService productService;
    private final ModelMapper modelMapper;

    public CartController(ProductService productService, ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/addProduct")
    @PreAuthorize("hasRole('CUSTOMER')")
    public String addToCartConfirm(@ModelAttribute(name="productId") Long productId, int quantity, HttpSession session) {
        this.initCart(session);
        ProductDetailsViewModel product = this.modelMapper
                .map(this.productService.findById(productId), ProductDetailsViewModel.class);

        CartViewModel cartViewModel = new CartViewModel();

        cartViewModel.setProductDetailsViewModel(product);
        cartViewModel.setQuantity(quantity);

        var cart = this.retrieveCart(session);
        this.addItemToCart(cartViewModel, cart);

        return "redirect:/products/";
    }

    @GetMapping("/details")
    @PreAuthorize("isAuthenticated()")
    public String cartDetails(Model model, HttpSession session) {
        var cart = this.retrieveCart(session);
        model.addAttribute("totalPrice", this.calcTotal(cart));

        return "cart/details";
    }

    private void initCart(HttpSession session) {
        if (session.getAttribute("shopping-cart") == null) {
            session.setAttribute("shopping-cart", new LinkedList<>());
        }
    }

    private void addItemToCart(CartViewModel cartViewModel, List<CartViewModel> cartViewModelList) {
        for (CartViewModel item : cartViewModelList) {
            if (item.getProductDetailsViewModel().getId().equals(cartViewModel.getProductDetailsViewModel().getId())) {
                item.setQuantity(item.getQuantity() + cartViewModel.getQuantity());
                return;
            }
        }

        cartViewModelList.add(cartViewModel);
    }

    private List<CartViewModel> retrieveCart(HttpSession session) {
        this.initCart(session);

        return (List<CartViewModel>) session.getAttribute("shopping-cart");
    }

    private BigDecimal calcTotal(List<CartViewModel> cart) {
        BigDecimal result = new BigDecimal(0);
        for (CartViewModel item : cart) {
            result = result.add(item.getProductDetailsViewModel().getPrice().multiply(new BigDecimal(item.getQuantity())));
        }

        return result;
    }

}
