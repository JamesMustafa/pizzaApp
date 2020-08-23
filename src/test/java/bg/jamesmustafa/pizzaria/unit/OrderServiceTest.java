package bg.jamesmustafa.pizzaria.unit;

import bg.jamesmustafa.pizzaria.db.entity.*;
import bg.jamesmustafa.pizzaria.db.repository.OrderRepository;
import bg.jamesmustafa.pizzaria.dto.binding.OrderBindingModel;
import bg.jamesmustafa.pizzaria.dto.binding.auth.UserServiceModel;
import bg.jamesmustafa.pizzaria.dto.view.OrderDetailsViewModel;
import bg.jamesmustafa.pizzaria.dto.view.OrderHistoryViewModel;
import bg.jamesmustafa.pizzaria.event.ApprovedOrderPublisher;
import bg.jamesmustafa.pizzaria.service.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.modelmapper.ModelMapper;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class OrderServiceTest {
    private OrderService serviceToTest;

    @Mock
    private OrderRepository mockOrderRepository;
    @Mock
    private ModelMapper mockModelMapper;
    @Mock
    private ApprovedOrderPublisher mockOrderPublisher;

    private long ORDER_ID = 99;
    private List<Order> orders;
    private List<Product> products;

    @BeforeEach
    public void setUp(){
        mockModelMapper = new ModelMapper();
        serviceToTest = new OrderService(mockOrderRepository,mockOrderPublisher,mockModelMapper);
    }

    @Test
    public void testAddOrderForApproval() throws Exception {
        //arrange
        OrderBindingModel order = this.mockModelMapper.map(createOrder(), OrderBindingModel.class);

        when(mockOrderRepository.save(any())).thenAnswer(
                (Answer<Order>) invocation -> {
                    Order orderToSave = invocation.getArgument(0);
                    orderToSave.setId(ORDER_ID);
                    return orderToSave;
                });

        //act
        Order actual = this.serviceToTest.addOrderForApproval(order);

        //assert
        Assertions.assertEquals(false, actual.getApproved());
        Assertions.assertEquals(order.getCustomer().getUsername(), actual.getCustomer().getUsername());
    }

    @Test
    public void testDeclineOrder() throws Exception {
        //arrange
        User testUser = createUser();
        Order order = new Order();
        order.setId(ORDER_ID);
        order.setTotalPrice(BigDecimal.valueOf(99.99));
        order.setComment("Nothing interesting again.");
        order.setCustomer(testUser);
        order.setApproved(false);
        order.setProducts(new ArrayList<>());

        when(mockOrderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));
        when(mockOrderRepository.save(any())).thenAnswer(
                (Answer<Order>) invocation -> {
                    Order orderToSave = invocation.getArgument(0);
                    orderToSave.setId(ORDER_ID);
                    return orderToSave;
                });
        //act
        this.serviceToTest.declineOrder(ORDER_ID);
        //assert
        Assertions.assertEquals(false, order.getSuccessful());
        Assertions.assertEquals(true, order.getApproved());
    }

    @Test
    public void testConfirmOrder() throws Exception {
        //arrange
        User testUser = createUser();
        Order order = new Order();
        order.setId(ORDER_ID);
        order.setTotalPrice(BigDecimal.valueOf(99.99));
        order.setComment("Nothing interesting again.");
        order.setCustomer(testUser);
        order.setApproved(false);
        order.setProducts(new ArrayList<>());

        when(mockOrderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));
        when(mockOrderRepository.save(any())).thenAnswer(
                (Answer<Order>) invocation -> {
                    Order orderToSave = invocation.getArgument(0);
                    orderToSave.setId(ORDER_ID);
                    return orderToSave;
                });
        //act
        this.serviceToTest.confirmOrder(ORDER_ID,"23:59");
        //assert
        Assertions.assertEquals(true, order.getSuccessful());
        Assertions.assertEquals(true, order.getApproved());
    }

    @Test
    public void testFindAll() throws Exception {
        //arrange
        Order firstOrder = createOrder();
        Order secondOrder = createOrder();
        Order thirdOrder = createOrder();
        orders = new ArrayList<>();
        orders.add(firstOrder);
        orders.add(secondOrder);
        orders.add(thirdOrder);
        when(mockOrderRepository.findAll()).thenReturn(orders);
        //act
        List<OrderBindingModel> actual = this.serviceToTest.findAll();
        //assert
        Assertions.assertEquals(3, actual.size());
        Assertions.assertEquals(firstOrder.getTotalPrice(), actual.get(0).getTotalPrice());
    }

    @Test
    public void testFindAllOrdersForApproval() throws Exception {
        //arrange
        Order firstOrder = createOrder();
        Order secondOrder = createOrder();
        secondOrder.setApproved(true);

        orders = new ArrayList<>();
        orders.add(firstOrder);
        orders.add(secondOrder);
        when(mockOrderRepository.findAll()).thenReturn(orders);
        //act
        List<OrderBindingModel> actual = this.serviceToTest.findAllOrdersForApproval();
        //assert
        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals(firstOrder.getApproved(), actual.get(0).getApproved());
    }

    @Test
    public void testFindOrdersByCustomer() throws Exception {
        //arrange
        Order firstOrder = createOrder();
        firstOrder.setApproved(false); // it does not count because approved should be true
        Order secondOrder = createOrder();
        secondOrder.setApproved(true);
        Order thirdOrder = createOrder();
        thirdOrder.getCustomer().setUsername("ANOTHER"); //it does not count either cuz the username we lookin is test

        orders = new ArrayList<>();
        orders.add(firstOrder);
        orders.add(secondOrder);
        orders.add(thirdOrder);
        when(mockOrderRepository.findAll()).thenReturn(orders);
        //act
        List<OrderHistoryViewModel> actual = this.serviceToTest.findOrdersByCustomer("test");
        //assert
        Assertions.assertEquals(1, actual.size());
    }

    @Test
    public void testFindOrderDetailsById() throws Exception {
        //arrange
        Product firstProduct = createProduct();
        Product secondProduct = createProduct();
        Product thirdProduct = createProduct();
        products = new ArrayList<>();
        products.add(firstProduct);
        products.add(secondProduct);
        products.add(thirdProduct);
        Order order = createOrder();
        order.setProducts(products);
        when(mockOrderRepository.findById(ORDER_ID)).thenReturn(Optional.of(order));
        //act
        OrderDetailsViewModel actual = this.serviceToTest.findOrderDetailsById(ORDER_ID);
        //assert
        Assertions.assertEquals(order.getId(), actual.getId());
        Assertions.assertEquals(products.size(), actual.getProducts().size());
    }

    @Test
    public void testFindById() throws Exception {
        //arrange
        Order test = createOrder();
        when(mockOrderRepository.findById(ORDER_ID)).thenReturn(Optional.of(test));
        //act
        OrderBindingModel actual = this.serviceToTest.findById(ORDER_ID);
        //assert
        Assertions.assertEquals(test.getTotalPrice(), actual.getTotalPrice());
        Assertions.assertEquals(test.getId(), actual.getId());

    }

    private User createUser(){
        //user
        User test = new User();
        test.setEmail("test@example.com");
        test.setUsername("test");
        test.setName("test");
        test.setSurname("test");
        test.setPhoneNumber("0894888210");
        test.setCity("test");
        test.setAddress("test");
        test.setPassword("test");
        test.setEmailConfirmed(false);
        test.setPhoneNumberConfirmed(false);
        test.setRoles(Set.of(new Role()));
        return test;
    }

    private Order createOrder(){
        User testUser = createUser();
        Order order = new Order();
        order.setId(ORDER_ID);
        order.setTotalPrice(BigDecimal.valueOf(99.99));
        order.setComment("Nothing interesting again.");
        order.setCustomer(testUser);
        order.setApproved(false);
        order.setProducts(new ArrayList<>());
        return order;
    }

    private Product createProduct(){
        Category fruit = new Category();
        fruit.setName("Fruits");

        Product product = new Product();
        product.setName("Banana");
        product.setCategory(fruit);
        product.setImgSrc("Not available");
        product.setDescription("Best bananas from madagascar for you.");
        product.setActivity(true);
        product.setId((long)1);
        product.setPrice(BigDecimal.valueOf(1.99));
        return product;
    }
}
