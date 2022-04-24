package pl.coderslab.service;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.entity.Role;
import pl.coderslab.entity.User;
import pl.coderslab.repository.RoleReposiotry;
import pl.coderslab.repository.UserRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;


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

    public List<User> findAll(){
        return userReposiotry.findAll();
    }

    User findByUsername(String login) { return userReposiotry.findByUsername(login); };

    public Optional<User> findByUserId (Long id) { return userReposiotry.findById(id); };

    public void update (User user){
        userReposiotry.save(user);
    }

//    public void saveuser (User user){
//        user.setUsername("userlogin");
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.setFirstName("UserName");
//        user.setLastName("UserSurname");
//        user.setEmail("usermail@m.pl");
//        user.setPhoneNumber(111222333);
//        Role userRole = roleReposiotry.findByName("ROLE_USER");
//        user.setRoles(new HashSet<>(Arrays.asList(userRole)));
//        userReposiotry.save(user);
//    }
//    public void saveadmin (User user){
//        user.setUsername("useradmin");
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.setFirstName("AdminName");
//        user.setLastName("AdminSurname");
//        user.setEmail("adminmail@m.pl");
//        user.setPhoneNumber(111222333);
//        Role userRole = roleReposiotry.findByName("ROLE_ADMIN");
//        user.setRoles(new HashSet<>(Arrays.asList(userRole)));
//        userReposiotry.save(user);
//    }


}
