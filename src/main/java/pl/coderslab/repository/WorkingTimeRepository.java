package pl.coderslab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.coderslab.entity.WorkingTime;

import java.util.List;

@Repository
public interface WorkingTimeRepository extends JpaRepository <WorkingTime, Long> {
    List<WorkingTime> findAllByInvestity_IdAndUser_Id (long investityId, long userId);
}
