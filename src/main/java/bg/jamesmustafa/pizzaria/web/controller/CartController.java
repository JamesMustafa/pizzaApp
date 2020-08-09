package bg.jamesmustafa.pizzaria.web.controller;

import bg.jamesmustafa.pizzaria.dto.binding.OrderBindingModel;
import bg.jamesmustafa.pizzaria.dto.binding.ProductBindingModel;
import bg.jamesmustafa.pizzaria.dto.binding.auth.UserServiceModel;
import bg.jamesmustafa.pizzaria.dto.view.CartViewModel;
import bg.jamesmustafa.pizzaria.dto.view.ProductDetailsViewModel;
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
    private final OrderService orderService;
    private final ModelMapper modelMapper;
    private final UserDetailsServiceImpl userDetailsService;

    public CartController(ProductService productService, OrderService orderService, ModelMapper modelMapper, UserDetailsServiceImpl userDetailsService) {
        this.productService = productService;
        this.orderService = orderService;
        this.modelMapper = modelMapper;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/addProduct")
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

    @PostMapping("/reOrder")
    public String reOrderConfirm(@ModelAttribute(name="orderId") Long orderId, HttpSession session) {

        OrderBindingModel orderBindingModel = this.orderService.findById(orderId);
        for (ProductBindingModel productDTO : orderBindingModel.getProducts()){

            this.initCart(session);
            ProductDetailsViewModel product = this.modelMapper
                    .map(productDTO, ProductDetailsViewModel.class);

            CartViewModel cartViewModel = new CartViewModel();
            cartViewModel.setProductDetailsViewModel(product);
            cartViewModel.setQuantity(1); //one because there is counter in addItemToCart method and it will collect all the same products


            var cart = this.retrieveCart(session);
            this.addItemToCart(cartViewModel, cart);
        }


        return "redirect:/cart/details";
    }

    @GetMapping("/details")
    public String cartDetails(Model model, HttpSession session) {

        var cart = this.retrieveCart(session);
        model.addAttribute("totalPrice", this.calcTotal(cart));

        return "cart/details";
    }

    //TODO: DeleteMapping and method="delete" are not working, but when it's Post everything is fine. Why is that?
    @PostMapping("/removeProduct")
    @PreAuthorize("hasRole('CUSTOMER')")
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

    private void removeItemFromCart(Long id, List<CartViewModel> cart) {
        cart.removeIf(c -> c.getProductDetailsViewModel().getId().equals(id));
    }

    private OrderBindingModel prepareOrder(List<CartViewModel> cart, String customer, String comment) {
        //TODO: Should I use service models in controller
        OrderBindingModel orderBindingModel = new OrderBindingModel();
        orderBindingModel.setComment(comment);
        orderBindingModel.setCustomer(this.modelMapper.map(this.userDetailsService.findUserByUsername(customer), UserServiceModel.class));
        List<ProductBindingModel> products = new ArrayList<>();
        for (CartViewModel item : cart) {
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
