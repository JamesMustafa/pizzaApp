package bg.jamesmustafa.pizzaria;

import bg.jamesmustafa.pizzaria.db.entity.ConfirmationToken;
import bg.jamesmustafa.pizzaria.db.entity.User;
import bg.jamesmustafa.pizzaria.db.repository.ConfirmationTokenRepository;
import bg.jamesmustafa.pizzaria.service.ConfirmationTokenService;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConfirmationTokenServiceTest {

    private ConfirmationTokenService serviceToTest;

    @Mock
    ConfirmationTokenRepository mockTokenRepository;

    @BeforeEach
    public void setUp() {
        serviceToTest = new ConfirmationTokenService(mockTokenRepository);
    }

    @Test
    public void testFindAll() {

    }
}

