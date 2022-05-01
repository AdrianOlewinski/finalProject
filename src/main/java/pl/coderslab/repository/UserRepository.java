package pl.coderslab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.coderslab.entity.Role;
import pl.coderslab.entity.User;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository <User, Long> {
    User findByUsername (String username);

    List<User> findAllByRolesIsIn(Set<Role>roles);


}
