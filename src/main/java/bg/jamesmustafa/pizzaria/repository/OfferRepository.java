package bg.jamesmustafa.pizzaria.repository;

import bg.jamesmustafa.pizzaria.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
}
