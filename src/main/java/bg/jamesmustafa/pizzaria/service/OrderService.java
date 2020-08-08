package bg.jamesmustafa.pizzaria.service;

import bg.jamesmustafa.pizzaria.data.dto.OrderDTO;
import bg.jamesmustafa.pizzaria.data.dto.ProductDTO;
import bg.jamesmustafa.pizzaria.entity.Order;
import bg.jamesmustafa.pizzaria.entity.User;
import bg.jamesmustafa.pizzaria.error.OrderNotFoundException;
import bg.jamesmustafa.pizzaria.repository.OrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final ModelMapper modelMapper;

    public OrderService(OrderRepository orderRepository, ProductService productService, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public void createOrder(OrderDTO orderDTO) {

        //Here we shouldn't create an order, but to update it, so the approve= true
        orderDTO.setSuccessful(true);
        Order approvedOrder = this.modelMapper.map(orderDTO, Order.class);
        this.orderRepository.saveAndFlush(approvedOrder);
    }

    public void addOrderForApproval(OrderDTO orderDTO){

        Order pendingOrder = this.modelMapper.map(orderDTO, Order.class);
        pendingOrder.setApproved(false);
        //pendingOrder.setProducts(this.productService.mapToListOfProducts(orderDTO.getProducts()));
        pendingOrder.setCustomer(this.modelMapper.map(orderDTO.getCustomer(), User.class));
        this.orderRepository.saveAndFlush(pendingOrder);
        //should create a list kydeto se trupat takiwa orderi za approvment ot employee.
        //toi trq da sloji vreme i da cukne dali ordera e successful ili ne e
        //trq mu izlezne specialen prozorec kyudeto da se pokaje ordera i choweka koito go e poruchal
    }


    public List<OrderDTO> findAllOrdersForApproval(){

        List<OrderDTO> ordersForApproval = this.findAll()
                .stream()
                .filter(o -> o.getApproved().equals(false))
                .collect(Collectors.toList());

        return ordersForApproval;
    }

    public List<OrderDTO> findAll() {
        List<Order> orders = this.orderRepository.findAll();
        List<OrderDTO> orderDTOList= orders
                .stream()
                .map(o -> this.modelMapper.map(o, OrderDTO.class))
                .collect(Collectors.toList());

        return orderDTOList;
    }

    public List<OrderDTO> findOrdersByCustomer(String username) {
        return this.orderRepository.findOrdersByCustomer(username)
                .stream()
                .map(o -> modelMapper.map(o, OrderDTO.class))
                .collect(Collectors.toList());
    }


    public OrderDTO findById(Long orderId) {
        return this.orderRepository.findById(orderId)
                .map(o -> this.modelMapper.map(o, OrderDTO.class))
                .orElseThrow(() -> new OrderNotFoundException("Order with this id has not been found!"));
    }

    public void declineOrder(Long orderId){

        OrderDTO orderDTO = this.findById(orderId);
        orderDTO.setApproved(true);
        orderDTO.setSuccessful(false);

        this.orderRepository.save(this.modelMapper.map(orderDTO, Order.class));
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

