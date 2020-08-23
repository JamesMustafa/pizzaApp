package bg.jamesmustafa.pizzaria.service;

import bg.jamesmustafa.pizzaria.db.entity.ConfirmationToken;
import bg.jamesmustafa.pizzaria.db.entity.User;
import bg.jamesmustafa.pizzaria.db.repository.ConfirmationTokenRepository;
import bg.jamesmustafa.pizzaria.error.ConfirmationTokenNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ConfirmationTokenService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfirmationTokenService.class);
    private final ConfirmationTokenRepository tokenRepository;

    public ConfirmationTokenService(ConfirmationTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public ConfirmationToken createToken(String tokenString, User user){
        ConfirmationToken token = new ConfirmationToken();
        token.setToken(tokenString);
        token.setUser(user);
        token.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        this.tokenRepository.save(token);
        LOGGER.info("Creating a new token for user with email {}.", user.getEmail());
        return token;
    }

    public boolean isTokenValid(String tokenString, String email){
        ConfirmationToken token = this.tokenRepository.findByToken(tokenString)
                .orElseThrow(() -> new ConfirmationTokenNotFoundException("Confirmation token with given token was not found!"));

        if(token.getExpiryDate().isAfter(LocalDateTime.now()) //checks if the token is expired
                && token.getUser().getEmail().equals(email) //checks if the emails match (catching him through principal, so he cant lie)
                && isTokenEmailUnique(email)){ //checks if the email is unique in the database
            return true;
        }
        else return false;
    }

    private boolean isTokenEmailUnique(String email){
        if(this.tokenRepository.findAll()
                .stream()
                .filter(t -> t.getUser().getEmail().equals(email)).count() == 1){
            return true;
        }
        else return false;
    }
}
