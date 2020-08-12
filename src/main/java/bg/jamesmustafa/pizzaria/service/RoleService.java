package bg.jamesmustafa.pizzaria.service;

import bg.jamesmustafa.pizzaria.db.entity.Role;
import bg.jamesmustafa.pizzaria.db.repository.RoleRepository;
import bg.jamesmustafa.pizzaria.error.RoleNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findRoleByName(String name){
        return this.roleRepository
                .findAll()
                .stream()
                .filter(r -> r.getName().equals(name))
                .findAny()
                .orElseThrow(() -> new RoleNotFoundException("Role with given id was not found!"));
    }
}
