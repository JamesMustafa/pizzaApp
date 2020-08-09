package bg.jamesmustafa.pizzaria.db.repository;

import bg.jamesmustafa.pizzaria.db.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
