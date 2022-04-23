package pl.coderslab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.coderslab.entity.Investity;

@Repository
public interface InvestityRepository extends JpaRepository <Investity, Long> {
    Investity findByInvestityName (String name);
}
