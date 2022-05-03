package pl.coderslab.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.entity.Investity;
import pl.coderslab.entity.User;
import pl.coderslab.exception.EntityNotFoundException;
import pl.coderslab.repository.InvestityRepository;
import pl.coderslab.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class InvestityService {

    private final InvestityRepository investityRepository;
    private final UserRepository userRepository;
    private final InvestityCostsService investityCostsService;
    private final WorkingTimeService workingTimeService;

    public InvestityService(InvestityRepository investityRepository, UserRepository userRepository,
                            InvestityCostsService investityCostsService, WorkingTimeService workingTimeService) {
        this.investityRepository = investityRepository;
        this.userRepository = userRepository;
        this.investityCostsService = investityCostsService;
        this.workingTimeService = workingTimeService;
    }

    public List<Investity> findAll(){
        return investityRepository.findAll();
    }

    public void addNewInvestity(Investity investity){
        investityRepository.save(investity);
    }
    public void editInvestity(Investity investity){
        investityRepository.save(investity);
    }
    public void deleteInvestityById(long id){
        investityRepository.deleteById(id);
    }
    public Optional<Investity> findById(long id){
        return investityRepository.findById(id);
    }

    public Map<Long, Double> getAllCosts(){
        return investityRepository.findAll().stream().collect(Collectors.toMap(s -> s.getId()
                ,s -> investityCostsService.sumOfAllInvestityCosts(s.getId())+workingTimeService.sumOfAllInvestityCost(s.getId())));
    }

    public Double getInvestityMargin(long id){
        double result;
        Optional<Investity> investity = investityRepository.findById(id);
        int budget = investity.orElseThrow(()->new EntityNotFoundException(id)).getBudget();
        int supplierCosts = investityCostsService.sumOfAllInvestityCosts(id);
        int userCosts = workingTimeService.getAllUserCosts(id);
        if(budget == 0){
            result = Double.POSITIVE_INFINITY;
        }else{
            result = ((Double.longBitsToDouble(budget) - Double.longBitsToDouble(supplierCosts)
                    - Double.longBitsToDouble(userCosts)) /Double.longBitsToDouble(budget))*100;
        }
        return Math.round(result*100.0)/100.0;
    }

    public Map<Long, Double> getAllMargins(){
        return investityRepository.findAll().stream().
                collect(Collectors.toMap(s->s.getId(),s->getInvestityMargin(s.getId())));
    }

    public boolean isInvestityNameTaken(Investity investity) {
        boolean result = false;
        if(investity.getId()==0){
            result = investityRepository.findByInvestityName(investity.getInvestityName()).isPresent();
        }else if(investity.getInvestityName().equals(investityRepository
                .findById(investity.getId()).orElseThrow(()->new EntityNotFoundException(investity.getId())).getInvestityName())){
            result = false;
        }else{
            result = investityRepository.findByInvestityName(investity.getInvestityName()).isPresent();
        }
        return result;
    }


}
