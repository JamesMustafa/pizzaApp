package bg.jamesmustafa.pizzaria.service;

import bg.jamesmustafa.pizzaria.data.models.service.UserDTO;
import bg.jamesmustafa.pizzaria.data.models.service.UserServiceModel;
import bg.jamesmustafa.pizzaria.entity.User;
import bg.jamesmustafa.pizzaria.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserDetailsServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return new UserDTO(user);
    }

    //TODO: Should there be two methods like that or i should make them one
    public UserServiceModel findUserByUsername(String username) throws UsernameNotFoundException{
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        UserServiceModel userServiceModel = this.modelMapper.map(user, UserServiceModel.class);
        return userServiceModel;
    }


}
