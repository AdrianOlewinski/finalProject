package pl.coderslab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.coderslab.entity.Investity;

import java.util.Optional;

@Repository
public interface InvestityRepository extends JpaRepository <Investity, Long> {
    Optional<Investity> findByInvestityName (String name);
}
