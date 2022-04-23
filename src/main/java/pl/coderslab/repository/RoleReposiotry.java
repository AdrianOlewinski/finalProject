package pl.coderslab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.coderslab.entity.Role;

@Repository
public interface RoleReposiotry extends JpaRepository <Role, Integer> {
    Role findByName (String name);
}
