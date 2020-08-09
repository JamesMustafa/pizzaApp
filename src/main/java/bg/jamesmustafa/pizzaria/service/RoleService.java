package bg.jamesmustafa.pizzaria.service;

import bg.jamesmustafa.pizzaria.entity.Role;
import bg.jamesmustafa.pizzaria.repository.RoleRepository;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findRoleByName(String name){

        Role role = this.roleRepository
                .findAll()
                .stream()
                .filter(r -> r.getName().equals(name))
                .findAny()
                .orElseThrow(); //TODO: Order not found exception

        return role;
    }
}
