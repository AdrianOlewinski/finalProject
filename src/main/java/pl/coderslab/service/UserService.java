package pl.coderslab.service;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.entity.Role;
import pl.coderslab.entity.Supplier;
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

    ;

    public Optional<User> findByUserId(Long id) {
        return userReposiotry.findById(id);
    }

    ;

    public void updateByUser(User user) {
        String password = userReposiotry.findById(user.getId()).get().getPassword();
        user.setPassword(password);
        Role userRole = roleReposiotry.findByName("ROLE_USER");
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));
        userReposiotry.save(user);
    }

    //    public void changePassword (User user, String oldPass, String newPass1, String newPass2){
//        String password = userReposiotry.findById(user.getId()).get().getPassword();;
//        if(passwordEncoder.matches(oldPass,password) && newPass1.equals(newPass2)){
//                user.setPassword(passwordEncoder.encode(newPass1));
//                userReposiotry.save(user);
//            }else{
//            System.out.println("Niepoprawne stare hasło lub nowe hasła się nie zgadzają");
//        }
//    }
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

    public void addNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleReposiotry.findByName("ROLE_USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        userReposiotry.save(user);
    }

    public void editUserByAdmin(User user) {
        String password = userReposiotry.findById(user.getId()).get().getPassword();
        user.setPassword(password);
        Role userRole = roleReposiotry.findByName("ROLE_USER");
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));
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
                .findById(user.getId()).orElseThrow(() -> new EntityNotFoundException(user.getId())).getUsername())) {
            result = false;
        } else {
            result = userReposiotry.findByUsername(user.getUsername()).isPresent();
        }
        return result;
    }

}
