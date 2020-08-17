package bg.jamesmustafa.pizzaria.web.controller;

import bg.jamesmustafa.pizzaria.dto.binding.OrderBindingModel;
import bg.jamesmustafa.pizzaria.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final ModelMapper modelMapper;

    public OrderController(OrderService orderService, ModelMapper modelMapper) {
        this.orderService = orderService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public String pending(Model model) {
        List<OrderBindingModel> pendingOrders = this.orderService.findAllOrdersForApproval();
        model.addAttribute("pendingOrders", pendingOrders);
        return "order/pendingOrders";
    }

    @GetMapping("/pending/{id}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public String pendingDetails(@PathVariable("id") Long orderId, Model model) {
        model.addAttribute("order", this.orderService.findOrderDetailsById(orderId));
        return "order/pendingDetails";
    }

    @GetMapping("/history")
    @PreAuthorize("hasRole('CUSTOMER')")
    public String getOrderHistory(Principal principal, Model model) {
        model.addAttribute("previousOrders", this.orderService.findOrdersByCustomer(principal.getName()));
        return "order/history";
    }

    @GetMapping("/history/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public String getOrderHistoryDetails(@PathVariable("id") Long orderId, Model model) {
        model.addAttribute("orderDetails", this.orderService.findOrderDetailsById(orderId));
        return "order/historyDetails";
    }

    @PostMapping("/declineOrder")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public String declineOrder(@ModelAttribute(name="orderDeclineId") Long orderId){
        this.orderService.declineOrder(orderId);
        return "redirect:/orders/pending";
    }

    @PostMapping("/confirmOrder")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public String confirmOrder(@ModelAttribute(name="orderConfirmId") Long orderId, String waitingTime){
        this.orderService.confirmOrder(orderId, waitingTime);
        return "redirect:/orders/pending";
    }

}
