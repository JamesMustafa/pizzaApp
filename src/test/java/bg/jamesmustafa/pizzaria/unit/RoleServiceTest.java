package bg.jamesmustafa.pizzaria.unit;

import bg.jamesmustafa.pizzaria.db.entity.Role;
import bg.jamesmustafa.pizzaria.db.repository.RoleRepository;
import bg.jamesmustafa.pizzaria.service.RoleService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class RoleServiceTest {

    private RoleService serviceToTest;

    @Mock
    private RoleRepository mockRoleRepository;

    @BeforeEach
    public void setUp(){
        serviceToTest = new RoleService(mockRoleRepository);
    }

    @Test
    public void testFindRoleByName() throws Exception {
        //arrange
        Role customer = new Role();
        customer.setName("ROLE_CUSTOMER");
        customer.setId((long)1);

        when(mockRoleRepository.findByName("ROLE_CUSTOMER")).thenReturn(Optional.of(customer));
        //act
        Role actual = this.serviceToTest.findRoleByName("ROLE_CUSTOMER");
        //assert
        Assertions.assertEquals(customer.getName(), actual.getName());
        Assertions.assertEquals(customer.getId(), actual.getId());

    }
}
