package pl.coderslab.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.entity.Investity;
import pl.coderslab.repository.InvestityRepository;
import pl.coderslab.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InvestityService {

    private final InvestityRepository investityRepository;
    private final UserRepository userRepository;

    public InvestityService(InvestityRepository investityRepository, UserRepository userRepository) {
        this.investityRepository = investityRepository;
        this.userRepository = userRepository;
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

}
