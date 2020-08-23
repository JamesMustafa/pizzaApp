package bg.jamesmustafa.pizzaria.unit;

import bg.jamesmustafa.pizzaria.db.entity.ConfirmationToken;
import bg.jamesmustafa.pizzaria.db.entity.Role;
import bg.jamesmustafa.pizzaria.db.entity.User;
import bg.jamesmustafa.pizzaria.db.repository.ConfirmationTokenRepository;
import bg.jamesmustafa.pizzaria.service.ConfirmationTokenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ConfirmationTokenServiceTest {

    private ConfirmationTokenService serviceToTest;

    @Mock
    private ConfirmationTokenRepository mockTokenRepository;

    private long TOKEN_ID = 99;
    private ArrayList<ConfirmationToken> tokens;
    private String TOKEN = "Some token here...";

    @BeforeEach
    public void setUp() {
        serviceToTest = new ConfirmationTokenService(mockTokenRepository);
    }

    @Test
    public void testCreateToken() throws Exception {
        //arrange
        when(mockTokenRepository.save(any())).thenAnswer(
                (Answer<ConfirmationToken>) invocation -> {
                    ConfirmationToken tokenToSave = invocation.getArgument(0);
                    tokenToSave.setId(TOKEN_ID);
                    return tokenToSave;
                });
        //act
        ConfirmationToken actual = this.serviceToTest.createToken(TOKEN, new User());
        //assert
        Assertions.assertEquals(TOKEN, actual.getToken());
        Assertions.assertEquals(TOKEN_ID, actual.getId());
    }

    @Test
    public void testIsTokenValid() throws Exception {
        //arrange
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
        //token
        ConfirmationToken token = new ConfirmationToken();
        token.setToken(TOKEN);
        token.setUser(test);
        token.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        token.setId(TOKEN_ID);
        //list of tokens
        tokens = new ArrayList<>();
        tokens.add(token);

        when(mockTokenRepository.findByToken(TOKEN)).thenReturn(Optional.of(token));
        when(mockTokenRepository.findAll()).thenReturn(tokens);

        //act
        boolean isValid = this.serviceToTest.isTokenValid(TOKEN, "test@example.com");

        //assert
        Assertions.assertTrue(isValid);
    }
}
