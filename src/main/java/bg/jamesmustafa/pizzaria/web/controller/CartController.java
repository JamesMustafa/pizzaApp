package bg.jamesmustafa.pizzaria.web.controller;

import bg.jamesmustafa.pizzaria.data.dto.OrderDTO;
import bg.jamesmustafa.pizzaria.data.dto.ProductDTO;
import bg.jamesmustafa.pizzaria.data.models.view.CartViewModel;
import bg.jamesmustafa.pizzaria.data.models.view.ProductDetailsViewModel;
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
    @PreAuthorize("hasRole('CUSTOMER')")
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
    @PreAuthorize("hasRole('CUSTOMER')")
    public String checkoutConfirm(String comment, HttpSession session, Principal principal) {
        var cart = this.retrieveCart(session);

        OrderDTO orderDTO = this.prepareOrder(cart, principal.getName(), comment);
        this.orderService.addOrderForApproval(orderDTO);
        //this.orderService.createOrder(orderDTO); --this thing will be implemented when the employee accepts.
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

    private OrderDTO prepareOrder(List<CartViewModel> cart, String customer, String comment) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setComment(comment);
        orderDTO.setCustomer(this.userDetailsService.findUserByUsername(customer));
        List<ProductDTO> products = new ArrayList<>();
        for (CartViewModel item : cart) {
            ProductDTO productDTO = this.modelMapper.map(item.getProductDetailsViewModel(), ProductDTO.class);

            for (int i = 0; i < item.getQuantity(); i++) {
                products.add(productDTO);
            }
        }

        orderDTO.setProducts(products);
        orderDTO.setTotalPrice(this.calcTotal(cart));

        return orderDTO;
    }

}
