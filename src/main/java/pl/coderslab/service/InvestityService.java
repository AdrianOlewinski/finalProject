package pl.coderslab.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.entity.Investity;
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

    public InvestityService(InvestityRepository investityRepository,
                            UserRepository userRepository, InvestityCostsService investityCostsService) {
        this.investityRepository = investityRepository;
        this.userRepository = userRepository;
        this.investityCostsService = investityCostsService;
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

    public Map<Long, Integer> getAllCosts(){
        return investityRepository.findAll().stream().collect(Collectors.toMap(s -> s.getId()
                ,s -> investityCostsService.sumOfAllInvestityCosts(s.getId())));
    }

    public Double getInvestityMargin(long id){
        double result;
        Optional<Investity> investity = investityRepository.findById(id);
        int budget = investity.get().getBudget();
        int costs = investityCostsService.sumOfAllInvestityCosts(id);
        if(budget == 0){
            result = Double.POSITIVE_INFINITY;
        }else{
            result = ((Double.longBitsToDouble(budget) - Double.longBitsToDouble(costs))
                    /Double.longBitsToDouble(budget))*100;
        }
        return result;
    }

    public Map<Long, Double> getAllMargins(){
        return investityRepository.findAll().stream().
                collect(Collectors.toMap(s->s.getId(),s->getInvestityMargin(s.getId())));
    }

}
