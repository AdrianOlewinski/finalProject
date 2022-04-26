package pl.coderslab.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.entity.Investity;
import pl.coderslab.entity.User;
import pl.coderslab.entity.WorkingTime;
import pl.coderslab.exception.WorkingTimeNotFoundException;
import pl.coderslab.repository.InvestityCostsRepository;
import pl.coderslab.repository.InvestityRepository;
import pl.coderslab.repository.UserRepository;
import pl.coderslab.repository.WorkingTimeRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
        Investity investity = workingTimeRepository.findFirstByUser_IdOrderByLocalDateDesc(id)
                .orElseThrow(()->new WorkingTimeNotFoundException(id)).getInvestity();
        return investity;
    }

    private int sumOfAllHoursByUserInInvestity(long investityId, long userId){
        return workingTimeRepository.findAllByInvestity_IdAndUser_Id(investityId,userId).stream()
                .map(s->s.getNumberOfHours()).reduce(0,Integer::sum);
    }
    public Map<Long, Integer> getAllHoursByUserInInvestity(long investityId){
        return workingTimeRepository.findAllByInvestity_Id(investityId).stream()
                .collect(Collectors.toMap(s->s.getId(),s-> sumOfAllHoursByUserInInvestity(investityId,s.getId())));
    }

    private int sumOfAllCostsByUserInInvestity(long investityId, long userId){
        return workingTimeRepository.findAllByInvestity_IdAndUser_Id(investityId, userId).stream()
                .map(s->s.getSalaryPerHours()*s.getNumberOfHours()).reduce(0,Integer::sum);
    }
    public Map<Long, Integer> getAllCostsByUserInInvestity(long investityId){
        return workingTimeRepository.findAllByInvestity_Id(investityId).stream()
                .collect(Collectors.toMap(s->s.getId(),s-> sumOfAllCostsByUserInInvestity(investityId,s.getId())));
    }

    public int getAllUserCosts(long investityId){
        return workingTimeRepository.findAllByInvestity_Id(investityId).stream()
                .map(s->s.getSalaryPerHours()*s.getNumberOfHours()).reduce(0, Integer::sum);
    }

    public List<WorkingTime> findAllByInvestityId(long id){
        return workingTimeRepository.findAllByInvestity_Id(id);
    }

    public List<User> allUsersInInvestity(long investityId){
        return findAllByInvestityId(investityId)
                .stream().map(s->s.getUser()).distinct().collect(Collectors.toList());
    }




}
