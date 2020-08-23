package bg.jamesmustafa.pizzaria.integration;

import bg.jamesmustafa.pizzaria.db.entity.Order;
import bg.jamesmustafa.pizzaria.db.entity.User;
import bg.jamesmustafa.pizzaria.db.repository.OrderRepository;
import bg.jamesmustafa.pizzaria.db.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository mockOrderRepository;

    @Autowired
    private UserRepository mockUserRepository;

    private User admin;
    private long ID = 1;

    @BeforeEach
    public void setUp(){

        if(this.mockOrderRepository.count() == 0){
            Order testOrder = new Order();
            testOrder.setId(1L);
            testOrder.setApproved(false);
            //testOrder.setSuccessful(true);
            testOrder.setTotalPrice(BigDecimal.valueOf(13.00));
            //testOrder.setWaitingTime(LocalDateTime.now());
            testOrder.setComment("Nothing much");
            testOrder.setCustomer(admin);

            this.mockOrderRepository.save(testOrder);
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

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN", "ROLE_CUSTOMER", "ROLE_EMPLOYEE"})
    public void testPendingViewDetails() throws Exception {
        this.mockMvc.perform(get("/orders/pending/{id}", ID)).andExpect(status().isOk())
        .andExpect(view().name("orders/pending"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN", "ROLE_CUSTOMER", "ROLE_EMPLOYEE"})
    public void testDeclineOrder() throws Exception {
        this.mockMvc.perform(post("/orders/declineOrder")
                .param("orderDeclineId", "1L"))
                .andExpect(status().isOk())
                .andExpect(redirectedUrl("orders/pending"));
    }

}
