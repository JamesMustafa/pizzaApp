package bg.jamesmustafa.pizzaria.db.repository;

import bg.jamesmustafa.pizzaria.db.entity.ConfirmationToken;
import bg.jamesmustafa.pizzaria.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    Optional<ConfirmationToken> findByToken(String token);
    Optional<ConfirmationToken> findByUser(User user);
}
