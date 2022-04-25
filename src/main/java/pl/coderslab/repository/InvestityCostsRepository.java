package pl.coderslab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.coderslab.entity.InvestityCosts;

import java.util.List;

@Repository
public interface InvestityCostsRepository extends JpaRepository<InvestityCosts, Long> {

    List<InvestityCosts> findAllByInvestity_Id(long id);
}
