package pl.coderslab.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.entity.Investity;
import pl.coderslab.entity.WorkingTime;
import pl.coderslab.repository.InvestityCostsRepository;
import pl.coderslab.repository.InvestityRepository;
import pl.coderslab.repository.UserRepository;
import pl.coderslab.repository.WorkingTimeRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class WorkingTimeService {
    private final WorkingTimeRepository workingTimeRepository;
    private final UserRepository userRepository;
    private final InvestityRepository investityRepository;
    private final InvestityCostsRepository investityCostsRepository;

    public WorkingTimeService(WorkingTimeRepository workingTimeRepository, UserRepository userRepository,
                              InvestityRepository investityRepository,
                              InvestityCostsRepository investityCostsRepository) {
        this.workingTimeRepository = workingTimeRepository;
        this.userRepository = userRepository;
        this.investityRepository = investityRepository;
        this.investityCostsRepository = investityCostsRepository;
    }

    public void addNewWorkingTime (WorkingTime workingTime){
        workingTimeRepository.save(workingTime);
    }

    public void update (WorkingTime workingTime){
        workingTimeRepository.save(workingTime);
    }

    public void deleteById (Long id){
        workingTimeRepository.deleteById(id);
    }

    public List<WorkingTime> findByInvestity_IdAndUser_Id(long investityId, long userId){
        return workingTimeRepository.findAllByInvestity_IdAndUser_Id(investityId, userId);
    }

    public List<WorkingTime> findAllByUser_Id(long userId){
        return workingTimeRepository.findAllByUser_IdOrderByLocalDateAsc(userId);
    }

    public Optional findById(long id){
        return workingTimeRepository.findById(id);
    }

    public Investity findLatestInvestity(long id){
        Investity investity = workingTimeRepository.findFirstByUser_IdOrderByLocalDateDesc(id).get().getInvestity();
        return investity;
    }

}
