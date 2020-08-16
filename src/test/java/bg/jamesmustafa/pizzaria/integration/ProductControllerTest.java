package bg.jamesmustafa.pizzaria.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithAnonymousUser
    public void testAnonymousProductsAccess() throws Exception {

        mockMvc.perform(get("/products")).
                andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser(username = "admin", roles={"CUSTOMER", "EMPLOYEE","ADMIN"})
    public void testProductsIndexAccess() throws Exception {

        mockMvc.perform(get("/products")).
                andExpect(status().isOk()).
                andExpect(view().name("product/index")).
                andExpect(model().attributeExists("pizzas", "drinks", "salads", "deserts"));
    }

    @Test
    @WithMockUser(username = "james", roles={"CUSTOMER"})
    public void testAdminRoleAccessWithUser() throws Exception {

        mockMvc.perform(get("/products/add")).
                andExpect(status().isForbidden());
    }

}
