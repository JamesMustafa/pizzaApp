package bg.jamesmustafa.pizzaria;

import bg.jamesmustafa.pizzaria.entity.Category;
import bg.jamesmustafa.pizzaria.entity.Role;
import bg.jamesmustafa.pizzaria.entity.User;
import bg.jamesmustafa.pizzaria.repository.CategoryRepository;
import bg.jamesmustafa.pizzaria.repository.RoleRepository;
import bg.jamesmustafa.pizzaria.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class PizzariaApplicationInit implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public PizzariaApplicationInit(UserRepository userRepository, CategoryRepository categoryRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        if(categoryRepository.count() ==0){
            Category pizza = new Category();
            pizza.setName("Pizza");
            Category drinks = new Category();
            drinks.setName("Drinks");
            Category deserts = new Category();
            deserts.setName("Deserts");
            Category salads = new Category();
            salads.setName("Salads");

            categoryRepository.save(pizza);
            categoryRepository.save(drinks);
            categoryRepository.save(deserts);
            categoryRepository.save(salads);
        }
        if(userRepository.count() == 0){


            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            roleRepository.save(adminRole);

            Role customerRole = new Role();
            customerRole.setName("ROLE_CUSTOMER");
            roleRepository.save(customerRole);

            Role employeeRole = new Role();
            employeeRole.setName("ROLE_EMPLOYEE");
            roleRepository.save(employeeRole);


            //---- ADMIN ----
            User admin = new User();
            admin.setEmail("admin@example.com");
            admin.setUsername("admin");
            admin.setEmailConfirmed(true);
            admin.setPhoneNumberConfirmed(false);
            admin.setName("admin");
            admin.setSurname("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setCity("Asenovgrad");
            admin.setAddress("Marica 11");

            admin.setRoles(Set.of(adminRole, customerRole, employeeRole));
            userRepository.save(admin);

            // ---- CUSTOMER ------
            User customer = new User();
            customer.setEmail("customer@example.com");
            customer.setUsername("customer");
            customer.setEmailConfirmed(false);
            customer.setPhoneNumberConfirmed(false);
            customer.setName("customer");
            customer.setSurname("customer");
            customer.setPassword(passwordEncoder.encode("customer"));
            customer.setCity("Asenovgrad");
            customer.setAddress("Marica 11");

            customer.setRoles(Set.of(customerRole));
            userRepository.save(customer);


            // ----- EMPLOYEE ------
            User employee = new User();
            employee.setEmail("employee@example.com");
            employee.setUsername("employee");
            employee.setEmailConfirmed(false);
            employee.setPhoneNumberConfirmed(false);
            employee.setName("employee");
            employee.setSurname("employee");
            employee.setPassword(passwordEncoder.encode("employee"));
            employee.setCity("Asenovgrad");
            employee.setAddress("Marica 11");

            employee.setRoles(Set.of(employeeRole, customerRole));
            userRepository.save(employee);
        }

    }
}
