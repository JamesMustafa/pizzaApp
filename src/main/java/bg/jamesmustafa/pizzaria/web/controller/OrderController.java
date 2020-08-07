package bg.jamesmustafa.pizzaria.web.controller;

import bg.jamesmustafa.pizzaria.data.dto.OrderDTO;
import bg.jamesmustafa.pizzaria.service.OrderService;
import bg.jamesmustafa.pizzaria.service.ProductService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
