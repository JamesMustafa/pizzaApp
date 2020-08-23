package bg.jamesmustafa.pizzaria.unit;

import bg.jamesmustafa.pizzaria.db.entity.Role;
import bg.jamesmustafa.pizzaria.db.entity.User;
import bg.jamesmustafa.pizzaria.db.repository.RoleRepository;
import bg.jamesmustafa.pizzaria.db.repository.UserRepository;
import bg.jamesmustafa.pizzaria.dto.binding.auth.UserAddBindingModel;
import bg.jamesmustafa.pizzaria.dto.binding.auth.UserServiceModel;
import bg.jamesmustafa.pizzaria.service.RoleService;
import bg.jamesmustafa.pizzaria.service.UserDetailsServiceImpl;
import org.junit.Assert;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserDetailsServiceImplTest {

    private UserDetailsServiceImpl serviceToTest;

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private RoleService mockRoleService;

    @Mock
    private ModelMapper mockModelMapper;

    @Mock
    private PasswordEncoder mockPasswordEncoder;

    private long USER_ID = 99;

    @BeforeEach
    public void setUp() {
        mockModelMapper = new ModelMapper();
        serviceToTest = new UserDetailsServiceImpl(mockUserRepository,mockRoleService,mockModelMapper,mockPasswordEncoder);
    }

    @Test
    public void testFindByUsername() {
        //Arrange
        User admin = createUser();

        when(mockUserRepository.findByUsername("admin")).
                thenReturn(Optional.of(admin));

        // act
        User expected = this.serviceToTest.loadUserByUsername("admin");

        // assert
        Assertions.assertEquals(expected.getId(), admin.getId());
        Assertions.assertEquals(expected.getUsername(), admin.getUsername());
        Assertions.assertEquals(expected.getCity(), admin.getCity());
    }

    @Test
    public void testCreateUser() throws Exception {

        //Arrange
        UserAddBindingModel userModel = new UserAddBindingModel();
        userModel.setEmail("test@example.com");
        userModel.setUsername("test");
        userModel.setName("test");
        userModel.setSurname("test");
        userModel.setPassword("test");
        userModel.setConfirmPassword("test");
        userModel.setPhoneNumber("0894888310");
        userModel.setCity("test");
        userModel.setAddress("test");

        Role role = new Role();
        role.setName("ROLE_CUSTOMER");

        when(mockRoleService.findRoleByName("ROLE_CUSTOMER")).thenReturn(role);
        when(mockUserRepository.save(any())).thenAnswer(
        (Answer<User>) invocation -> {
        User userToSave = invocation.getArgument(0);
        userToSave.setId(USER_ID);
             return userToSave;
        });

        //Act
        User expected = this.serviceToTest.createUser(userModel);

        //Assert
        Assert.assertEquals(expected.getEmail(), userModel.getEmail());
        Assert.assertEquals(expected.getUsername(), userModel.getUsername());
        Assert.assertEquals(expected.getName(), userModel.getName());
    }

    @Test
    public void testFindUserById() throws Exception {
        //Arrange
        User admin = createUser();
        when(this.mockUserRepository.findById(USER_ID)).thenReturn(Optional.of(admin));

        // act
        User user = this.serviceToTest.findUserById(USER_ID);

        // assert
        Assertions.assertEquals(USER_ID, user.getId());
        Assertions.assertEquals(user.getUsername(), "admin");
        Assertions.assertEquals(user.getCity(), "Asenovgrad");
    }

    @Test
    public void testConfirmEmail() throws Exception {
        //Arrange
        User user = createUser();

        when(mockUserRepository.save(any())).thenAnswer(
                (Answer<User>) invocation -> {
                    User userToSave = invocation.getArgument(0);
                    userToSave.setId(USER_ID);
                    return userToSave;
                });

        //act
        this.serviceToTest.confirmUserEmail(user);

        //assert
        Assertions.assertEquals(user.getEmailConfirmed(), true);

    }

    @Test
    public void testEditUser() throws Exception {
        //arrange
        User oldUser = createUser();
        UserServiceModel newUser = new UserServiceModel();
        newUser.setEmail("test@example.com");
        newUser.setUsername("test");
        newUser.setName("test");
        newUser.setSurname("test");
        newUser.setPhoneNumber("0894888210");
        newUser.setCity("test");
        newUser.setAddress("test");

        when(mockUserRepository.findById(USER_ID)).thenReturn(Optional.of(oldUser));
        when(mockUserRepository.save(any())).thenAnswer(
                (Answer<User>) invocation -> {
                    User userToSave = invocation.getArgument(0);
                    userToSave.setId(USER_ID);
                    return userToSave;
                });

        //act
        this.serviceToTest.editUser(oldUser.getId(), newUser);

        //assert
        Assertions.assertEquals(oldUser.getCity(), newUser.getCity());
        Assertions.assertEquals(oldUser.getEmail(), newUser.getEmail());

    }

    private User createUser() {
        //---- ADMIN ----
        User admin = new User();
        admin.setId(USER_ID);
        admin.setEmail("admin@example.com");
        admin.setUsername("admin");
        admin.setPhoneNumber("0894888310");
        admin.setEmailConfirmed(false);
        admin.setPhoneNumberConfirmed(false);
        admin.setName("admin");
        admin.setSurname("admin");
        admin.setPassword(this.mockPasswordEncoder.encode("admin"));
        admin.setCity("Asenovgrad");
        admin.setAddress("Marica 11");

        admin.setRoles(Set.of(new Role()));
        this.mockUserRepository.save(admin);

        return admin;
    }
}



