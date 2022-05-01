package pl.coderslab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.coderslab.entity.User;
import pl.coderslab.entity.WorkingTime;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkingTimeRepository extends JpaRepository <WorkingTime, Long> {
    List<WorkingTime> findAllByInvestity_IdAndUser_IdOrderByLocalDateDesc(long investityId, long userId);
    List<WorkingTime> findAllByUser_IdOrderByLocalDateDesc(long userId);
    Optional<WorkingTime> findFirstByUser_IdOrderByLocalDateDesc (long userId);
    List<WorkingTime> findAllByInvestity_Id(long investityId);

    List<WorkingTime> findAllByLocalDate(LocalDate localDate);

    List<User> findByInvestity_Id(long ivestityId);

    List<WorkingTime> findAllByUser_Id(long userId);

}
