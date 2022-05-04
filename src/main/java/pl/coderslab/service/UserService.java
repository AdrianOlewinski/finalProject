package pl.coderslab.service;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.entity.Role;
import pl.coderslab.entity.User;
import pl.coderslab.exception.EntityNotFoundException;
import pl.coderslab.repository.RoleReposiotry;
import pl.coderslab.repository.UserRepository;

import java.util.*;


@Service
@Transactional
public class UserService {

    private final UserRepository userReposiotry;
    private final RoleReposiotry roleReposiotry;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userReposiotry, RoleReposiotry roleReposiotry, BCryptPasswordEncoder passwordEncoder) {
        this.userReposiotry = userReposiotry;
        this.roleReposiotry = roleReposiotry;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        return userReposiotry.findAll();
    }

    public User findByUsername(String login) {
        return userReposiotry
                .findByUsername(login).orElseThrow(() -> new EntityNotFoundException(login));
    }

    public Optional<User> findByUserId(Long id) {
        return userReposiotry.findById(id);
    }


    public void update(User user) {
        String password = userReposiotry.findById(user.getId()).get().getPassword();
        user.setPassword(password);
        Set<Role> userRoles = userReposiotry.findById(user.getId())
                .orElseThrow(()-> new EntityNotFoundException("Could not find user id =" + user.getId())).getRoles();
        user.setRoles(userRoles);
        userReposiotry.save(user);
    }

    public void changePassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userReposiotry.save(user);
    }

    public List<User> findAllAdmins() {
        Role role = roleReposiotry.findByName("ROLE_ADMIN");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        return userReposiotry.findAllByRolesIsIn(roles);
    }

    public List<User> findAllUsers() {
        Role role = roleReposiotry.findByName("ROLE_USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        return userReposiotry.findAllByRolesIsIn(roles);
    }

    public List<User> findInactiveUsers() {
        Role role = roleReposiotry.findByName("ROLE_INACTIVE");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        return userReposiotry.findAllByRolesIsIn(roles);
    }

    public void addNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleReposiotry.findByName("ROLE_USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        userReposiotry.save(user);
    }


    public void deleteUserById(long id) {
        Optional<User> user = userReposiotry.findById(id);
        Role role = roleReposiotry.findByName("ROLE_INACTIVE");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.get().setRoles(roles);
    }

    public boolean isUsernameTaken(User user) {
        boolean result = false;
        if (user.getId() == 0) {
            result = userReposiotry.findByUsername(user.getUsername()).isPresent();
        } else if (user.getUsername().equals(userReposiotry
                .findById(user.getId()).orElseThrow(() ->
                        new EntityNotFoundException("Could not found user " + user.getId())).getUsername())) {
            result = false;
        } else {
            result = userReposiotry.findByUsername(user.getUsername()).isPresent();
        }
        return result;
    }

    public void changeRole (long userId){
        User user = userReposiotry.findById(userId)
                .orElseThrow(()->new EntityNotFoundException("Could not find user id= "+userId));
        Set<Role> roles = user.getRoles();
        if (roles.contains(roleReposiotry.findByName("ROLE_USER"))){
            roles.remove(roleReposiotry.findByName("ROLE_USER"));
            roles.add(roleReposiotry.findByName("ROLE_ADMIN"));
            user.setRoles(roles);
        }else if(roles.contains(roleReposiotry.findByName("ROLE_ADMIN"))){
            roles.remove(roleReposiotry.findByName("ROLE_ADMIN"));
            roles.add(roleReposiotry.findByName("ROLE_USER"));
            user.setRoles(roles);
        }else if(roles.contains(roleReposiotry.findByName("ROLE_INACTIVE"))){
            roles.remove(roleReposiotry.findByName("ROLE_INACTIVE"));
            roles.add(roleReposiotry.findByName("ROLE_USER"));
            user.setRoles(roles);
        }
    }


}
