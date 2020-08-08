package bg.jamesmustafa.pizzaria.web.controller;

import bg.jamesmustafa.pizzaria.data.dto.OrderDTO;
import bg.jamesmustafa.pizzaria.service.OrderService;
import bg.jamesmustafa.pizzaria.service.ProductService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final ProductService productService;
    private final OrderService orderService;

    public OrderController(ProductService productService, OrderService orderService) {
        this.productService = productService;
        this.orderService = orderService;
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/pending")
    public String pending(Model model) {

        List<OrderDTO> pendingOrders = this.orderService.findAllOrdersForApproval();
        model.addAttribute("pendingOrders", pendingOrders);

        return "order/pendingOrders";
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/pending/{id}")
    public String pendingDetails(@PathVariable("id") Long orderId, Model model) {

        OrderDTO orderDTO = this.orderService.findById(orderId);
        model.addAttribute("order", orderDTO);

        return "order/pendingDetails";
    }

    @PostMapping("/declineOrder")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public String declineOrder(@ModelAttribute(name="orderDeclineId") Long orderId){

        this.orderService.declineOrder(orderId);
        return "redirect:/home";
    }

    @PostMapping("/confirmOrder")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public String confirmOrder(@ModelAttribute(name="orderConfirmId") Long orderId, String waitingTime){

        this.orderService.confirmOrder(orderId, waitingTime);
        return "redirect:/home";
    }
}
