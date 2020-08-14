package bg.jamesmustafa.pizzaria.web.controller;
import bg.jamesmustafa.pizzaria.dto.binding.OrderBindingModel;
import bg.jamesmustafa.pizzaria.service.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.security.Principal;

@Controller
@RequestMapping("/cart")
@PreAuthorize("hasRole('CUSTOMER')")
public class CartController {

    private final ProductService productService;
    private final OfferService offerService;
    private final OrderService orderService;
    private final CartService cartService;

    public CartController(ProductService productService, OfferService offerService, OrderService orderService, CartService cartService) {
        this.productService = productService;
        this.offerService = offerService;
        this.orderService = orderService;
        this.cartService = cartService;
    }

    @GetMapping("/details")
    public String cartDetails(Model model, HttpSession session) {
        var cart = this.cartService.retrieveCart(session);
        model.addAttribute("totalPrice", this.cartService.calcTotal(cart));
        return "cart/details";
    }

    @GetMapping("/details/offer/{id}")
    public String cartOfferDetails(@PathVariable("id") Long offerId, Model model, HttpSession session){
        model.addAttribute("offer", this.offerService.findById(offerId));
        return "cart/offerDetails";
    }

    @PostMapping("/addProduct")
    public String addToCartConfirm(@ModelAttribute(name="productId") Long productId, int quantity, HttpSession session) {
        this.cartService.addOneProductToCart(this.productService.findById(productId), quantity, session);
        return "redirect:/products";
    }

    @PostMapping("/reOrder")
    public String reOrderConfirm(@ModelAttribute(name="orderId") Long orderId, HttpSession session) {
        OrderBindingModel orderBindingModel = this.orderService.findById(orderId);
        this.cartService.addListOfProductsToCart(orderBindingModel.getProducts(), session);
        return "redirect:/cart/details";
    }

    //TODO: @DeleteMapping and method="delete" are not working, but when it's Post everything is fine. Why is that?
    @PostMapping("/removeProduct")
    public String removeFromCartConfirm(@ModelAttribute(name="deleteId") Long deleteId, HttpSession session) {
        this.cartService.removeItemFromCart(deleteId, this.cartService.retrieveCart(session));
        return "redirect:/cart/details";
    }

    @PostMapping("/checkout")
    public String checkoutConfirm(String comment, BigDecimal price, HttpSession session, Principal principal) {
        var cart = this.cartService.retrieveCart(session);
        OrderBindingModel orderBindingModel = this.cartService.prepareOrder(cart, principal.getName(), comment, price);
        this.orderService.addOrderForApproval(orderBindingModel);
        session.removeAttribute("shopping-cart");
        return "redirect:/home";
    }

    @PostMapping("/checkoutOffer")
    public String checkoutConfirm(Long offerId, String comment, BigDecimal price, Principal principal) {
        OrderBindingModel orderBindingModel = this.cartService.prepareOrderFromOffer(offerId, comment, price, principal.getName());
        this.orderService.addOrderForApproval(orderBindingModel);
        return "redirect:/home";
    }
}
