package bg.jamesmustafa.pizzaria.service;

import bg.jamesmustafa.pizzaria.dto.binding.OrderBindingModel;
import bg.jamesmustafa.pizzaria.dto.view.OrderDetailsViewModel;
import bg.jamesmustafa.pizzaria.dto.view.OrderHistoryViewModel;
import bg.jamesmustafa.pizzaria.dto.view.ProductDetailsViewModel;
import bg.jamesmustafa.pizzaria.db.entity.Order;
import bg.jamesmustafa.pizzaria.db.entity.User;
import bg.jamesmustafa.pizzaria.error.OrderNotFoundException;
import bg.jamesmustafa.pizzaria.db.repository.OrderRepository;
import bg.jamesmustafa.pizzaria.event.ApprovedOrderPublisher;
import bg.jamesmustafa.pizzaria.util.TimeUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    //TODO: This service returns a couple of view models instead of binding models. should check it again...
    private final OrderRepository orderRepository;
    private final ApprovedOrderPublisher orderPublisher;
    private final ModelMapper modelMapper;

    @PersistenceContext
    private EntityManager entityManager;

    public OrderService(OrderRepository orderRepository, ApprovedOrderPublisher orderPublisher, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.orderPublisher = orderPublisher;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public void addOrderForApproval(OrderBindingModel orderBindingModel){
        Order pendingOrder = this.modelMapper.map(orderBindingModel, Order.class);
        pendingOrder.setApproved(false);
        pendingOrder.setCustomer(this.modelMapper.map(orderBindingModel.getCustomer(), User.class));
        this.orderRepository.save(pendingOrder);
    }

    @Transactional
    public void declineOrder(Long orderId){
        Order order  = this.orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with this id does not exist!"));

        order.setApproved(true);
        order.setSuccessful(false);

        //In this way we are making optimistic locking to this entity.
        this.entityManager.lock(order, LockModeType.OPTIMISTIC);

        //publish event
        this.orderPublisher.publishDecline(order.getCustomer().getEmail(),
                order.getId().toString());

        this.orderRepository.save(order);
    }

    @Transactional
    public void confirmOrder(Long orderId, String waitingTime){
        Order order = this.orderRepository
                .findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with this id does not exist!"));

        order.setApproved(true);
        order.setSuccessful(true);
        order.setWaitingTime(TimeUtil.parseTimeToDate(waitingTime));

        //In this way we are making optimistic locking to this entity.
        this.entityManager.lock(order, LockModeType.OPTIMISTIC);

        //publish event
        this.orderPublisher.publishSuccess(order.getCustomer().getEmail(),
                order.getId().toString(),
                order.getWaitingTime().toString(),
                order.getTotalPrice().toString());

        this.orderRepository.save(order);
    }

    public List<OrderBindingModel> findAllOrdersForApproval(){
        return this.findAll()
                .stream()
                .filter(o -> o.getApproved().equals(false))
                .collect(Collectors.toList());
    }

    public List<OrderBindingModel> findAll() {
        return this.orderRepository.findAll()
                .stream()
                .map(o -> this.modelMapper.map(o, OrderBindingModel.class))
                .collect(Collectors.toList());
    }

    public List<OrderHistoryViewModel> findOrdersByCustomer(String username) {
         return this.orderRepository.findAll()
                .stream()
                .filter(o -> o.getCustomer().getUsername().equals(username))
                .filter(o -> o.getApproved().equals(true)) //the order should be approved in order to show in the history !
                .map(o -> this.modelMapper.map(o, OrderHistoryViewModel.class))
                .collect(Collectors.toList());
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
        //Checks if the customer has added some comment to the order, and if not, puts this comment below in order to give the employee additional info.
        if(orderDetailsViewModel.getComment().isEmpty()){
            orderDetailsViewModel.setComment("You do not have any comments about this order!");
        }

        return orderDetailsViewModel;
    }

    public OrderBindingModel findById(Long orderId) {
        return this.orderRepository.findById(orderId)
                .map(o -> this.modelMapper.map(o, OrderBindingModel.class))
                .orElseThrow(() -> new OrderNotFoundException("Order with this id has not been found!"));
    }
}

