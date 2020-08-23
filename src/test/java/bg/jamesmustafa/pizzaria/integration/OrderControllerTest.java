package bg.jamesmustafa.pizzaria.integration;

import bg.jamesmustafa.pizzaria.db.entity.Order;
import bg.jamesmustafa.pizzaria.db.entity.Role;
import bg.jamesmustafa.pizzaria.db.entity.User;
import bg.jamesmustafa.pizzaria.db.repository.OrderRepository;
import bg.jamesmustafa.pizzaria.db.repository.UserRepository;
import bg.jamesmustafa.pizzaria.service.OrderService;
import bg.jamesmustafa.pizzaria.web.controller.OrderController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ModelMapper mockModelMapper;

    @Autowired
    private OrderService mockOrderService;

    @Autowired
    private OrderRepository mockOrderRepository;

    @Autowired
    private UserRepository mockUserRepository;

    private User admin;
    private long ORDER_ID = 1;
    private Order testOrder;

    @BeforeEach
    public void setUp(){

        if(this.mockUserRepository.count() == 0){
            //---- ADMIN ----
            admin = new User();
            admin.setId(ORDER_ID);
            admin.setEmail("djem_mustafa@abv.bg");
            admin.setUsername("admin");
            admin.setEmailConfirmed(false);
            admin.setPhoneNumberConfirmed(false);
            admin.setName("admin");
            admin.setSurname("admin");
            admin.setPassword("123456");
            admin.setCity("Asenovgrad");
            admin.setAddress("Marica 11");
            admin.setRoles(Set.of(new Role()));
            this.mockUserRepository.save(admin);
        }

        if(this.mockOrderRepository.count() == 0){
            testOrder = new Order();
            testOrder.setId(ORDER_ID);
            testOrder.setApproved(false);
            testOrder.setProducts(new ArrayList<>());
            testOrder.setVersion(0);
            //testOrder.setSuccessful(true);
            testOrder.setTotalPrice(BigDecimal.valueOf(13.00));
            //testOrder.setWaitingTime(LocalDateTime.now());
            testOrder.setComment("Nothing much");
            testOrder.setCustomer(admin);

            this.mockOrderRepository.saveAndFlush(testOrder);
        }
    }

    @AfterEach
    public void setDown(){
    }

    @Test
    public void testDoesItCreateEntityInDB() throws Exception {
        Assertions.assertTrue(this.mockOrderRepository.count() > 0);
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN", "ROLE_CUSTOMER", "ROLE_EMPLOYEE"})
    public void testPendingView() throws Exception {
        this.mockMvc.perform(get("/orders/pending")).andExpect(status().isOk());
    }

//    @Test
//    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN", "ROLE_CUSTOMER", "ROLE_EMPLOYEE"})
//    public void testPendingViewDetails() throws Exception {
//        this.mockMvc.perform(get("/orders/pending/{id}", ORDER_ID)).andExpect(status().isOk())
//        .andExpect(view().name("orders/pending"));
//    }

    //Should have real database for this one...
//    @Test
//    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN", "ROLE_CUSTOMER", "ROLE_EMPLOYEE"})
//    public void testDeclineOrder() throws Exception {
//        this.mockMvc.perform(post("/orders/declineOrder")
//                .param("orderDeclineId", "1"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(redirectedUrl("orders/pending"));
//    }

}
