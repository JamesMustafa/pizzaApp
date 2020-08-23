//package bg.jamesmustafa.pizzaria.integration;
//
//import bg.jamesmustafa.pizzaria.db.entity.Category;
//import bg.jamesmustafa.pizzaria.db.entity.Product;
//import bg.jamesmustafa.pizzaria.db.repository.ProductRepository;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.CacheControl;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithAnonymousUser;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class ProductControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ProductRepository mockProductRepository;
//
//    @BeforeEach
//    public void setUp(){
//        if(this.mockProductRepository.count() == 0){
//            Product apple = new Product();
//            apple.setId((long)1);
//            apple.setName("Apple");
//            apple.setDescription("Try our fresh apples now");
//            apple.setActivity(true);
//            apple.setImgSrc("images/product/apple.png");
//            apple.setPrice(BigDecimal.valueOf(3.00));
//            apple.setCategory(new Category());
//            this.mockProductRepository.save(apple);
//        }
//    }
//
//    @AfterEach
//    public void setDown(){
//        this.mockProductRepository.deleteAll();
//    }
//
//    @Test
//    public void testDoesItCreateEntityInDB() throws Exception{
//        Assertions.assertTrue(this.mockProductRepository.count() > 0);
//    }
//
//    @Test
//    @WithAnonymousUser
//    public void testAnonymousProductsAccess() throws Exception {
//        this.mockMvc.perform(get("/products")).
//                andExpect(redirectedUrl("http://localhost/login"));
//    }
//
//    @Test
//    @WithMockUser(username = "admin", roles={"CUSTOMER", "EMPLOYEE", "ADMIN"})
//    public void testProductsIndexAccess() throws Exception {
//        this.mockMvc.perform(get("/products")).
//                andExpect(status().isOk()).
//                andExpect(view().name("product/index")).
//                andExpect(model().attributeExists("pizzas", "drinks", "salads", "deserts"));
//    }
//
//    @Test
//    @WithMockUser(username = "james", roles={"CUSTOMER"})
//    public void testAdminRoleAccessWithUser() throws Exception {
//        this.mockMvc.perform(get("/products/add"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(view().name("error/403"));
//    }
//
//    @Test
//    @WithMockUser(username = "admin", roles = {"ADMIN"})
//    public void testValidProductAdd() throws Exception {
//        this.mockMvc.perform(
//                post("/products/add").
//                        param("name", "krusha").
//                        param("description", "nai vkusnata krusha koqto shte opitate").
//                        param("price", "1.99").
//                        param("activity", "true").
//                        param("imgSrc", "images/product/krusha.jpeg").
//                        param("category.id", "3"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(view().name("redirect:/products"));
//    }
//
//
//    @Test
//    @WithMockUser(username = "admin", roles = {"ADMIN"})
//    public void testInvalidProductAdd() throws Exception {
//        this.mockMvc.perform(
//                post("/products/add").
//                        param("name", "krusha").
//                        param("description", "nai vkusnata krusha!").
//                        param("price", "1,39").
//                        param("activity", "true").
//                        param("imgSrc", "images/product/krusha.jpeg").
//                        param("category.id", "3"))
//                .andExpect(model().hasErrors())
//                .andExpect(view().name("product/createProduct"));
//    }
//
//
//
//    @Test
//    @WithMockUser(username = "admin", roles={"ADMIN"})
//    public void testGetEditViewWithInvalidId() throws Exception{
//        this.mockMvc.perform(get("/products/edit/{id}",99))
//                .andExpect(view().name("error/404"));
//    }
//
//}
