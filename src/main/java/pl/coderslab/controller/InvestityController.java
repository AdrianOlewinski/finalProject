package pl.coderslab.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.coderslab.entity.*;
import pl.coderslab.exception.InvestityCostsNotFoundException;
import pl.coderslab.exception.InvestityNotFoundException;
import pl.coderslab.exception.SupplierNotFoundException;
import pl.coderslab.exception.UserNotFoundException;
import pl.coderslab.repository.InvestityRepository;
import pl.coderslab.service.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class InvestityController {

    private final InvestityService investityService;
    private final SupplierService supplierService;
    private final WorkingTimeService workingTimeService;
    private final InvestityCostsService investityCostsService;
    private final UserService userService;


    public InvestityController(InvestityService investityService, SupplierService supplierService,
                               WorkingTimeService workingTimeService, InvestityCostsService investityCostsService,
                               UserService userService) {
        this.investityService = investityService;
        this.supplierService = supplierService;
        this.workingTimeService = workingTimeService;
        this.investityCostsService = investityCostsService;
        this.userService = userService;
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
        Investity investity = investityService.findById(id).orElseThrow(()->new InvestityNotFoundException(id));
        List<InvestityCosts> investityCosts = investityCostsService.findAllByInvestityId(id);
        int sumOfSuppliersCosts = investityCostsService.sumOfAllInvestityCosts(id);
        int sumOfUserCosts = workingTimeService.getAllUserCosts(id);
        double margin = investityService.getInvestityMargin(id);

        List<User> allUsers = workingTimeService.allUsersInInvestity(id);
        Map<Long, Integer> allHoursByUserInInvestity = workingTimeService.getAllHoursByUserInInvestity(id);
        Map<Long, Integer> getAllCostsByUserInInvestity = workingTimeService.getAllCostsByUserInInvestity(id);

        model.addAttribute("investity",investity);
        model.addAttribute("investityCosts",investityCosts);
        model.addAttribute("sumOfSuppliersCosts",sumOfSuppliersCosts);
        model.addAttribute("margin",margin);

        model.addAttribute("sumOfUserCosts",sumOfUserCosts);
        model.addAttribute("allUsers",allUsers);
        model.addAttribute("allHoursByUserInInvestity",allHoursByUserInInvestity);
        model.addAttribute("getAllCostsByUserInInvestity",getAllCostsByUserInInvestity);
        return "admin/investity/info";
    }

    @GetMapping(path = "admin/investity/info/editcost")
    @Secured("ROLE_ADMIN")
    String editInvestityCosts(@RequestParam long id, Model model){
        Optional<InvestityCosts> investityCosts = investityCostsService.findCosts(id);
        model.addAttribute("investityCosts", investityCosts
                .orElseThrow(()-> new InvestityCostsNotFoundException(id)));
        return "admin/investity/info/editcost";
    }
    @PostMapping(path = "admin/investity/info/editcost")
    @Secured("ROLE_ADMIN")
    String editInvestityCosts(@ModelAttribute InvestityCosts investityCosts){
        investityCostsService.save(investityCosts);
        return "redirect:/admin/investity/info?id="+investityCosts.getInvestity().getId();
    }

    @GetMapping(path = "admin/investity/info/deletecost")
    @Secured("ROLE_ADMIN")
    String deleteInvestityCosts(@RequestParam long id){
        long redirectId = investityCostsService.findCosts(id).orElseThrow(
                ()->new InvestityCostsNotFoundException(id)).getInvestity().getId();
        investityCostsService.deleteById(id);
        return "redirect:/admin/investity/info?id="+redirectId;
    }
    @GetMapping(path = "admin/investity/info/addcost")
    @Secured("ROLE_ADMIN")
    String addInvestityCosts(Model model, @RequestParam long investityId){
        InvestityCosts investityCosts = new InvestityCosts();
        investityCosts.setInvestity(investityService.findById(investityId)
                .orElseThrow(()->new InvestityNotFoundException(investityId)));
        model.addAttribute("investityCosts",investityCosts);
        return "admin/investity/info/addcost";
    }
    @PostMapping(path = "admin/investity/info/addcost")
    @Secured("ROLE_ADMIN")
    String addInvestityCosts(@ModelAttribute InvestityCosts investityCosts){
        investityCostsService.save(investityCosts);
        return "redirect:/admin/investity/info?id="+investityCosts.getInvestity().getId();
    }

    @GetMapping(path = "admin/investity/info/showuserinfo")
    @Secured("ROLE_ADMIN")
    String showUserInfoInInvestity(@RequestParam long userid, @RequestParam long investityid, Model model){
        List<WorkingTime> workingTime = workingTimeService.findByInvestity_IdAndUser_Id(investityid,userid);
        model.addAttribute("workingTime",workingTime);
        model.addAttribute("user",userService.findByUserId(userid)
                .orElseThrow(()->new UserNotFoundException(userid)));
        model.addAttribute("investity",investityService.findById(investityid)
                .orElseThrow(()->new InvestityNotFoundException(investityid)));
        return "admin/investity/info/showuserinfo";
    }

    @ModelAttribute("suppliers")
    Collection<Supplier> findAllSuppliers(){
        return supplierService.findAll();
    }
}
