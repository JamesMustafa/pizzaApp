package bg.jamesmustafa.pizzaria.web.controller;

import bg.jamesmustafa.pizzaria.data.dto.OrderDTO;
import bg.jamesmustafa.pizzaria.data.models.view.OrderDetailsViewModel;
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

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/history")
    public String getOrderHistory(Principal principal, Model model)
    {

        model.addAttribute("previousOrders", this.orderService.findOrdersByCustomer(principal.getName()));

        return "order/history";
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/history/{id}")
    public String getOrderHistoryDetails(@PathVariable("id") Long orderId, Model model)
    {
        model.addAttribute("orderDetails", this.orderService.findOrderDetailsById(orderId));

        return "order/historyDetails";
    }
}
