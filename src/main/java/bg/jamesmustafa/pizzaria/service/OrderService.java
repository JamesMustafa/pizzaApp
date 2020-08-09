package bg.jamesmustafa.pizzaria.service;

import bg.jamesmustafa.pizzaria.data.dto.OrderDTO;
import bg.jamesmustafa.pizzaria.data.dto.ProductDTO;
import bg.jamesmustafa.pizzaria.data.models.view.OrderDetailsViewModel;
import bg.jamesmustafa.pizzaria.data.models.view.OrderHistoryViewModel;
import bg.jamesmustafa.pizzaria.data.models.view.ProductDetailsViewModel;
import bg.jamesmustafa.pizzaria.entity.Order;
import bg.jamesmustafa.pizzaria.entity.User;
import bg.jamesmustafa.pizzaria.error.OrderNotFoundException;
import bg.jamesmustafa.pizzaria.repository.OrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    public OrderService(OrderRepository orderRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public void addOrderForApproval(OrderDTO orderDTO){

        Order pendingOrder = this.modelMapper.map(orderDTO, Order.class);
        pendingOrder.setApproved(false);
        pendingOrder.setCustomer(this.modelMapper.map(orderDTO.getCustomer(), User.class));
        this.orderRepository.saveAndFlush(pendingOrder);
    }


    public List<OrderDTO> findAllOrdersForApproval(){

        List<OrderDTO> ordersForApproval = this.findAll()
                .stream()
                .filter(o -> o.getApproved().equals(false))
                .collect(Collectors.toList());

        return ordersForApproval;
    }

    public List<OrderDTO> findAll() {

        List<OrderDTO> orderDTOList= this.orderRepository.findAll()
                .stream()
                .map(o -> this.modelMapper.map(o, OrderDTO.class))
                .collect(Collectors.toList());

        return orderDTOList;
    }

    public List<OrderHistoryViewModel> findOrdersByCustomer(String username) {

        List<OrderHistoryViewModel> orderHistoryViewModelList = this.orderRepository.findAll()
                .stream()
                .filter(o -> o.getCustomer().getUsername().equals(username))
                .filter(o -> o.getApproved().equals(true)) //the order should be approved in order to show in the history !
                .map(o -> this.modelMapper.map(o, OrderHistoryViewModel.class))
                .collect(Collectors.toList());

        return orderHistoryViewModelList;
    }
    public OrderDetailsViewModel findOrderDetailsById(Long orderId){

        Order order = this.orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with this id has not been found!"));

        OrderDetailsViewModel orderDetailsViewModel = this.modelMapper.map(order, OrderDetailsViewModel.class);

        orderDetailsViewModel.setProducts(
                order.getProducts()
                .stream()
                .map(o -> this.modelMapper.map(o, ProductDetailsViewModel.class))
                .collect(Collectors.toList())
        );

        if(orderDetailsViewModel.getWaitingTime() != null){
            orderDetailsViewModel
                    .setWaitingMinutes
                            (Duration.between(orderDetailsViewModel.getCreatedOn(), orderDetailsViewModel.getWaitingTime()).toMinutes());
        }
        if(orderDetailsViewModel.getComment().isEmpty()){
            orderDetailsViewModel.setComment("You do not have any comments about this order!");
        }

        return orderDetailsViewModel;
    }


    public OrderDTO findById(Long orderId) {

        OrderDTO order = this.orderRepository.findById(orderId)
                .map(o -> this.modelMapper.map(o, OrderDTO.class))
                .orElseThrow(() -> new OrderNotFoundException("Order with this id has not been found!"));

        return order;
    }

    public void declineOrder(Long orderId){

        Order order= this.orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with this id does not exist!"));

        order.setApproved(true);
        order.setSuccessful(false);

        this.orderRepository.save(order);
    }

    public void confirmOrder(Long orderId, String waitingTime){

        Order order = this.orderRepository
                .findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with this id does not exist!"));

        order.setApproved(true);
        order.setSuccessful(true);
        order.setWaitingTime(parseTime(waitingTime));
        //sent an email to the customer

        this.orderRepository.save(order);
    }

    private LocalDateTime parseTime(String time)
    {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String confirmDate = LocalDateTime.now().format(dateFormatter).toString();
        String waitingDateAndTime = confirmDate + " " + time;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(waitingDateAndTime, dateTimeFormatter);

        return dateTime;
    }


}

