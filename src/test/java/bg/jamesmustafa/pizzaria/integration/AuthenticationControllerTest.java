package bg.jamesmustafa.pizzaria.integration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import bg.jamesmustafa.pizzaria.db.entity.Role;
import bg.jamesmustafa.pizzaria.db.entity.User;
import bg.jamesmustafa.pizzaria.db.repository.RoleRepository;
import bg.jamesmustafa.pizzaria.db.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Set;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository mockUserRepository;

    @Autowired
    private RoleRepository mockRoleRepository;

    @Autowired
    private PasswordEncoder mockPasswordEncoder;

    private long NEW_USER_ID = 1;
    private User admin;

    @BeforeEach
    public void setUp() {

        if(this.mockRoleRepository.count() == 0){
            Role adminR = new Role();
            adminR.setName("ROLE_ADMIN");
            this.mockRoleRepository.save(adminR);

            Role customerRole = new Role();
            customerRole.setName("ROLE_CUSTOMER");
            this.mockRoleRepository.save(customerRole);

            Role employeeRole = new Role();
            employeeRole.setName("ROLE_EMPLOYEE");
            this.mockRoleRepository.save(employeeRole);
        }

        if(this.mockUserRepository.count() == 0){
            //---- ADMIN ----
            admin = new User();
            admin.setId(NEW_USER_ID);
            admin.setEmail("djem_mustafa@abv.bg");
            admin.setUsername("admin");
            admin.setEmailConfirmed(false);
            admin.setPhoneNumberConfirmed(false);
            admin.setName("admin");
            admin.setSurname("admin");
            admin.setPassword(mockPasswordEncoder.encode("admin"));
            admin.setCity("Asenovgrad");
            admin.setAddress("Marica 11");
            admin.setRoles(Set.of());
            this.mockUserRepository.save(admin);
        }
    }

    @Test
    public void testDoesItCreateEntityInDB() throws Exception{
        Assertions.assertTrue(this.mockUserRepository.count() > 0);
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    public void testIfForbiddenToLoginWhenAuthenticated() throws Exception{
        this.mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/403"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ROLE_ADMIN")
    public void testIfForbiddenToRegisterWhenAuthenticated() throws Exception{
        this.mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/403"));
    }

    @Test
    public void testIfAnonymousUserAccessDenied() throws Exception{
        this.mockMvc.perform(get("/authentication/profile")).
                andExpect(status().is3xxRedirection()).
                andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void testIfYouCanLogin() throws Exception{
        this.mockMvc.perform(post("/login")
                .param("username", "admin")
                .param("password", "admin"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void testIfYouCanRegister() throws Exception{
        this.mockMvc.perform(post("/register")
                .param("username", "Ivan98")
                .param("name", "Ivan")
                .param("surname", "Petrov")
                .param("email", "ivan_ivanov@gmail.com")
                .param("password", "123456")
                .param("confirmPassword", "123456")
                .param("city", "Sofia")
                .param("address", "Studentski grad, bl.61")
                .param("phoneNumber", "0895883732"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN", "ROLE_CUSTOMER"})
    public void testEditView() throws Exception{
        this.mockMvc.perform(get("/authentication/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("authenticate/edit"));
    }

}
