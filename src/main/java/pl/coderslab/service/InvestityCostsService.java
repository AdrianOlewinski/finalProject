package pl.coderslab.service;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.entity.InvestityCosts;
import pl.coderslab.repository.InvestityCostsRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class InvestityCostsService {
    private final InvestityCostsRepository investityCostsRepository;

    public InvestityCostsService(InvestityCostsRepository investityCostsRepository) {
        this.investityCostsRepository = investityCostsRepository;
    }

    public void save (InvestityCosts investityCosts){
        investityCostsRepository.save(investityCosts);
    }

    public void edit (InvestityCosts investityCosts){
        investityCostsRepository.save(investityCosts);
    }

    public void deleteById (long id){
        investityCostsRepository.deleteById(id);
    }

    public Optional<InvestityCosts> findCosts (long id){
       return investityCostsRepository.findById(id);
    }

    public List<InvestityCosts> findAllByInvestityId (long id){
        return investityCostsRepository.findAllByInvestity_Id(id);
    }

    public int sumOfAllInvestityCosts(long id){
        return investityCostsRepository.findAllByInvestity_Id(id).stream().map(s -> s.getCost()).reduce(0, Integer::sum);
    }

}
