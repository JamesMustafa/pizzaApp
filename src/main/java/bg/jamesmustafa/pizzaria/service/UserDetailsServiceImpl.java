package bg.jamesmustafa.pizzaria.service;

import bg.jamesmustafa.pizzaria.data.models.binding.UserAddBindingModel;
import bg.jamesmustafa.pizzaria.data.models.service.UserDTO;
import bg.jamesmustafa.pizzaria.data.models.service.UserServiceModel;
import bg.jamesmustafa.pizzaria.entity.Role;
import bg.jamesmustafa.pizzaria.entity.User;
import bg.jamesmustafa.pizzaria.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    public UserDetailsServiceImpl(UserRepository userRepository, RoleService roleService, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserDTO loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return new UserDTO(user);
    }

    //TODO: Should there be two methods like that or i should make them one
    public User findUserByUsername(String username) throws UsernameNotFoundException{
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return user;
    }

    public boolean isUsernameAvailable(String username){
        return userRepository.findByUsername(username).isPresent();
    }


    public void createAndLoginUser(UserAddBindingModel userModel) {
        User newUser = createUser(userModel);
        UserDTO user = loadUserByUsername(newUser.getUsername());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user,
                userModel.getPassword(),
                user.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private User createUser(UserAddBindingModel userModel) {
        LOGGER.info("Creating a new user with username [GDPR].");

        User user = this.modelMapper.map(userModel, User.class);

        if (userModel.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userModel.getPassword()));
        }

        user.setEmailConfirmed(false);
        user.setPhoneNumberConfirmed(false);


        Role customerRole = this.roleService.findRoleByName("ROLE_CUSTOMER");

        user.setRoles(Set.of(customerRole));

        return userRepository.save(user);
    }

    public User findUserById(Long userId){

        User user = this.userRepository.findById(userId).orElseThrow(); //TODO: user not found exception create

        return user;
    }

    public void editUser(Long userId, UserServiceModel userModel){

        User user = this.userRepository
                .findById(userId)
                .orElseThrow(); // user not found exception create

        user.setName(userModel.getName());
        user.setSurname(userModel.getSurname());

        if(!user.getEmail().equals(userModel.getEmail())){
            user.setEmail(userModel.getEmail());
            user.setEmailConfirmed(false);
        }

        if(!user.getPhoneNumber().equals(userModel.getPhoneNumber())){
            user.setPhoneNumber(userModel.getPhoneNumber());
            user.setPhoneNumberConfirmed(false);
        }

        user.setCity(userModel.getCity());
        user.setAddress(userModel.getAddress());

        this.userRepository.save(user);
    }





}
