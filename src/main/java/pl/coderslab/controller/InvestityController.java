package pl.coderslab.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.coderslab.entity.Investity;
import pl.coderslab.entity.InvestityCosts;
import pl.coderslab.entity.Supplier;
import pl.coderslab.repository.InvestityRepository;
import pl.coderslab.service.InvestityCostsService;
import pl.coderslab.service.InvestityService;
import pl.coderslab.service.SupplierService;
import pl.coderslab.service.WorkingTimeService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class InvestityController {

    private final InvestityService investityService;
    private final SupplierService supplierService;
    private final WorkingTimeService workingTimeService;
    private final InvestityCostsService investityCostsService;


    public InvestityController(InvestityService investityService, SupplierService supplierService,
                               WorkingTimeService workingTimeService, InvestityCostsService investityCostsService) {
        this.investityService = investityService;
        this.supplierService = supplierService;
        this.workingTimeService = workingTimeService;
        this.investityCostsService = investityCostsService;
    }

    @GetMapping(path = "admin/investity/add")
    @Secured("ROLE_ADMIN")
    String addNewInvestity(Model model){
        Investity investity = new Investity();
        model.addAttribute("investity",investity);
        return "admin/investity/add";
    }

    @PostMapping(path = "admin/investity/add")
    @Secured("ROLE_ADMIN")
    String addNewInvestity(@ModelAttribute Investity investity){
        investityService.addNewInvestity(investity);
        return "redirect:/admin/investity/all";
    }

    @GetMapping(path = "admin/investity/all")
    @Secured("ROLE_ADMIN")
    String showAllInvestities(Model model){
        List<Investity> investities = investityService.findAll();
        Map<Long,Integer> allCosts = investityService.getAllCosts();
        Map<Long,Double> allMargins = investityService.getAllMargins();
        model.addAttribute("investities",investities);
        model.addAttribute("allCosts", allCosts);
        model.addAttribute("allMargins",allMargins);
        return "admin/investity/all";
    }

    @GetMapping(path = "admin/investity/edit")
    @Secured("ROLE_ADMIN")
    String editInvestity(Model model, @RequestParam long id){
        Optional<Investity> investity = investityService.findById(id);
        model.addAttribute("investity",investity.get());
        return "admin/investity/edit";
    }

    @PostMapping(path = "admin/investity/edit")
    @Secured("ROLE_ADMIN")
    String editInvestity(@ModelAttribute Investity investity){
        investityService.editInvestity(investity);
        return "redirect:/admin/investity/all";
    }

    @GetMapping(path = "admin/investity/delete")
    @Secured("ROLE_ADMIN")
    String deleteInvestity(@RequestParam long id){
        investityService.deleteInvestityById(id);
        return "redirect:/admin/investity/all";
    }

    @GetMapping(path = "admin/investity/info")
    @Secured("ROLE_ADMIN")
    String showInfo(@RequestParam long id, Model model){
        Optional <Investity> investity = investityService.findById(id);
        List<InvestityCosts> investityCosts = investityCostsService.findAllByInvestityId(id);
        int sumOfCosts = investityCostsService.sumOfAllInvestityCosts(id);
        double margin = investityService.getInvestityMargin(id);
        model.addAttribute("investity",investity.get());
        model.addAttribute("investityCosts",investityCosts);
        model.addAttribute("sumOdAllCosts",sumOfCosts);
        model.addAttribute("margin",margin);
        return "admin/investity/info";
    }
}
