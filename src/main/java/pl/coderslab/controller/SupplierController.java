package pl.coderslab.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.coderslab.entity.Supplier;
import pl.coderslab.service.SupplierService;

import java.util.List;
import java.util.Optional;

@Controller
public class SupplierController {
    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping(path = "admin/supplier/add")
    @Secured("ROLE_ADMIN")
    String addNewSupplier(Model model){
        Supplier supplier = new Supplier();
        model.addAttribute("supplier",supplier);
        return "admin/supplier/add";
    }

    @PostMapping(path = "admin/supplier/add")
    @Secured("ROLE_ADMIN")
    String addNewSupplier(@ModelAttribute Supplier supplier){
        supplierService.addNewSupplier(supplier);
        return "redirect:/admin/supplier/all";
    }

    @GetMapping(path = "admin/supplier/all")
    @Secured("ROLE_ADMIN")
    String showAllSuppliers(Model model){
        List<Supplier> suppliers = supplierService.findAll();
        model.addAttribute("suppliers",suppliers);
        return "admin/supplier/all";
    }

    @GetMapping(path = "admin/supplier/edit")
    @Secured("ROLE_ADMIN")
    String editSupplier(Model model, @RequestParam long id){
        Optional<Supplier> supplier = supplierService.findById(id);
        model.addAttribute("supplier",supplier.get());
        return "admin/supplier/edit";
    }

    @PostMapping(path = "admin/supplier/edit")
    @Secured("ROLE_ADMIN")
    String editSupplier(@ModelAttribute Supplier supplier){
        supplierService.editSupplier(supplier);
        return "redirect:/admin/supplier/all";
    }

    @GetMapping(path = "admin/supplier/delete")
    @Secured("ROLE_ADMIN")
    String deleteSupplier(@RequestParam long id){
        supplierService.deleteSupplierById(id);
        return "redirect:/admin/supplier/all";
    }
}
