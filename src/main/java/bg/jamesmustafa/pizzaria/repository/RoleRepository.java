package bg.jamesmustafa.pizzaria.repository;

import bg.jamesmustafa.pizzaria.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
