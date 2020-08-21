package bg.jamesmustafa.pizzaria.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithAnonymousUser
    public void testAnonymousProductsAccess() throws Exception {
        this.mockMvc.perform(get("/products")).
                andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser(username = "admin", roles={"CUSTOMER", "EMPLOYEE", "ADMIN"})
    public void testProductsIndexAccess() throws Exception {
        this.mockMvc.perform(get("/products")).
                andExpect(status().isOk()).
                andExpect(view().name("product/index")).
                andExpect(model().attributeExists("pizzas", "drinks", "salads", "deserts"));
    }

    @Test
    @WithMockUser(username = "james", roles={"CUSTOMER"})
    public void testAdminRoleAccessWithUser() throws Exception {
        this.mockMvc.perform(get("/products/add"))
                .andExpect(view().name("error/403"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testValidProductAdd() throws Exception {
        this.mockMvc.perform(
                post("/products/add").
                        contentType(MediaType.APPLICATION_FORM_URLENCODED).
                        param("name", "krusha").
                        param("description", "nai vkusnata krusha na trakiyskata nizina").
                        param("price", "1.99").
                        param("activity", "true").
                        param("imgSrc", "images/product/krusha.jpeg").
                        param("category.id", "3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/products"));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testInvalidProductAdd() throws Exception {
        this.mockMvc.perform(
                post("/products/add").
                        contentType(MediaType.APPLICATION_FORM_URLENCODED).
                        param("name", "kr").
                        param("description", "nai vkusnata krusha na trakiyskata nizina").
                        param("price", "1,39").
                        param("activity", "true").
                        param("imgSrc", "images/product/krusha.jpeg").
                        param("category.id", "3"))
                .andExpect(model().hasErrors())
                .andExpect(view().name("product/createProduct"));
    }



    @Test
    @WithMockUser(username = "admin", roles={"ADMIN"})
    public void testGetEditViewWithInvalidId() throws Exception{
        this.mockMvc.perform(get("/products/edit/{id}",99))
                .andExpect(view().name("error/404"));
    }

    @Test
    @WithMockUser(username = "admin", roles={"ADMIN"})
    public void testDeleteProduct() throws Exception{
        this.mockMvc.perform(post("/products/delete")
                .param("deleteId", "26"));
    }


}
