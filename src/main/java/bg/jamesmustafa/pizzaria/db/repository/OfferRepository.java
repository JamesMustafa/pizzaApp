package bg.jamesmustafa.pizzaria.db.repository;

import bg.jamesmustafa.pizzaria.db.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
}
