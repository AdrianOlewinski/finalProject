package pl.coderslab.service;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.entity.Supplier;
import pl.coderslab.repository.InvestityRepository;
import pl.coderslab.repository.SupplierInvestityRepository;
import pl.coderslab.repository.SupplierRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SupplierService {
    private final SupplierRepository supplierRepository;
    private final SupplierInvestityRepository supplierInvestityRepository;
    private final InvestityRepository investityRepository;

    public SupplierService(SupplierRepository supplierRepository, SupplierInvestityRepository supplierInvestityRepository,
                           InvestityRepository investityRepository) {
        this.supplierRepository = supplierRepository;
        this.supplierInvestityRepository = supplierInvestityRepository;
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
}
