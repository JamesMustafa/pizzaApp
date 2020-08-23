package bg.jamesmustafa.pizzaria.service;

import bg.jamesmustafa.pizzaria.db.entity.Role;
import bg.jamesmustafa.pizzaria.db.repository.RoleRepository;
import bg.jamesmustafa.pizzaria.error.RoleNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findRoleByName(String name){
        return this.roleRepository.findByName(name)
                .orElseThrow(() -> new RoleNotFoundException("Role with given name was not found!"));


    }
}
