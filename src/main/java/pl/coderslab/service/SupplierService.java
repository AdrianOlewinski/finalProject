package pl.coderslab.service;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.entity.Supplier;
import pl.coderslab.exception.EntityNotFoundException;
import pl.coderslab.repository.InvestityRepository;
import pl.coderslab.repository.InvestityCostsRepository;
import pl.coderslab.repository.SupplierRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SupplierService {
    private final SupplierRepository supplierRepository;
    private final InvestityCostsRepository InvestityCostsRepository;
    private final InvestityRepository investityRepository;

    public SupplierService(SupplierRepository supplierRepository, pl.coderslab.repository.InvestityCostsRepository investityCostsRepository, InvestityRepository investityRepository) {
        this.supplierRepository = supplierRepository;
        InvestityCostsRepository = investityCostsRepository;
        this.investityRepository = investityRepository;
    }

    public List<Supplier> findAll(){
        return supplierRepository.findAll();
    }

    public void addNewSupplier(Supplier supplier){
        supplierRepository.save(supplier);
    }

    public void editSupplier(Supplier supplier){
        supplierRepository.save(supplier);
    }

    public void deleteSupplierById(long id){
        supplierRepository.deleteById(id);
    }

    public Optional<Supplier> findById(long id){
        return supplierRepository.findById(id);
    }
    public boolean isSupplierNameTaken(Supplier supplier) {
        boolean result = false;
        if(supplier.getId()==0){
            result = supplierRepository.findByName(supplier.getName()).isPresent();
        }else if(supplier.getName().equals(supplierRepository
                .findById(supplier.getId())
                .orElseThrow(()->new EntityNotFoundException("Could not found investity " + supplier.getId())).getName())){
            result = false;
        }else{
            result = supplierRepository.findByName(supplier.getName()).isPresent();
        }
        return result;
    }
}
