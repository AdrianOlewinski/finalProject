package pl.coderslab.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.coderslab.entity.*;
import pl.coderslab.exception.EntityNotFoundException;
import pl.coderslab.service.*;

import javax.validation.Valid;
import java.time.DayOfWeek;
import java.time.Month;
import java.util.*;

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
    String addNewInvestity(Model model) {
        Investity investity = new Investity();
        model.addAttribute("investity", investity);
        return "admin/investity/add";
    }

    @PostMapping(path = "admin/investity/add")
    @Secured("ROLE_ADMIN")
    String addNewInvestity(@Valid Investity investity, BindingResult result) {
        if (investityService.isInvestityNameTaken(investity)) {
            result.addError(new FieldError("investity", "investityName"
                    , "Nazwa inwestycji jest zajęta!"));
        }
        if (result.hasErrors()) {
            return "admin/investity/add";
        }
        investityService.addNewInvestity(investity);
        return "redirect:/admin/investity/all";
    }

    @GetMapping(path = "admin/investity/all")
    @Secured("ROLE_ADMIN")
    String showAllInvestities(Model model) {
        List<Investity> investities = investityService.findAll();
        Map<Long, Double> allCosts = investityService.getAllCosts();
        Map<Long, Double> allMargins = investityService.getAllMargins();
        model.addAttribute("investities", investities);
        model.addAttribute("allCosts", allCosts);
        model.addAttribute("allMargins", allMargins);
        return "admin/investity/all";
    }

    @GetMapping(path = "admin/investity/edit")
    @Secured("ROLE_ADMIN")
    String editInvestity(Model model, @RequestParam long id) {
        Optional<Investity> investity = investityService.findById(id);
        model.addAttribute("investity"
                , investity.orElseThrow(() -> new EntityNotFoundException("Could not found investity " + id)));
        return "admin/investity/edit";
    }

    @PostMapping(path = "admin/investity/edit")
    @Secured("ROLE_ADMIN")
    String editInvestity(@Valid Investity investity, BindingResult result) {
        if (investityService.isInvestityNameTaken(investity)) {
            result.addError(new FieldError("investity", "investityName"
                    , "Nazwa inwestycji jest zajęta!"));
        }
        if (result.hasErrors()) {
            return "admin/investity/add";
        }
        investityService.editInvestity(investity);
        return "redirect:/admin/investity/all";
    }

    @GetMapping(path = "admin/investity/delete")
    @Secured("ROLE_ADMIN")
    String deleteInvestity(@RequestParam long id) {
        if (investityCostsService.findAllByInvestityId(id).size() > 0 || workingTimeService.findAllByInvestityId(id).size() > 0) {
            return "redirect:/admin/error?id=1";
        }
        investityService.deleteInvestityById(id);
        return "redirect:/admin/investity/all";
    }

    @GetMapping(path = "admin/investity/info")
    @Secured("ROLE_ADMIN")
    String showInfo(@RequestParam long id, Model model) {
        Investity investity = investityService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Could not found investity " + id));
        List<InvestityCosts> investityCosts = investityCostsService.findAllByInvestityId(id);
        int sumOfSuppliersCosts = investityCostsService.sumOfAllInvestityCosts(id);
        int sumOfUserCosts = workingTimeService.getAllUserCosts(id);
        double margin = investityService.getInvestityMargin(id);

        List<User> allUsers = workingTimeService.allUsersInInvestity(id);
        Map<Long, Double> allHoursByUserInInvestityMultiplierOne
                = workingTimeService.getAllHoursByUserInInvestityWithMultiplierOne(id);
        Map<Long, Double> allHoursByUserInInvestityMultiplierOther
                = workingTimeService.getAllHoursByUserInInvestityWithOtherMultiplier(id);
        Map<Long, Double> getAllCostsByUserInInvestity = workingTimeService.getAllCostsByUserInInvestity(id);

        model.addAttribute("investity", investity);
        model.addAttribute("investityCosts", investityCosts);
        model.addAttribute("sumOfSuppliersCosts", sumOfSuppliersCosts);
        model.addAttribute("margin", margin);

        model.addAttribute("sumOfUserCosts", sumOfUserCosts);
        model.addAttribute("allUsers", allUsers);
        model.addAttribute("allHoursByUserInInvestityMultiplierOne", allHoursByUserInInvestityMultiplierOne);
        model.addAttribute("allHoursByUserInInvestityMultiplierOther", allHoursByUserInInvestityMultiplierOther);
        model.addAttribute("getAllCostsByUserInInvestity", getAllCostsByUserInInvestity);
        return "admin/investity/info";
    }

    @GetMapping(path = "admin/investity/info/editcost")
    @Secured("ROLE_ADMIN")
    String editInvestityCosts(@RequestParam long id, Model model) {
        Optional<InvestityCosts> investityCosts = investityCostsService.findCosts(id);
        model.addAttribute("investityCosts", investityCosts
                .orElseThrow(() -> new EntityNotFoundException("Could not found investity costs " + id)));
        return "admin/investity/info/editcost";
    }

    @PostMapping(path = "admin/investity/info/editcost")
    @Secured("ROLE_ADMIN")
    String editInvestityCosts(@Valid InvestityCosts investityCosts, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/investity/info/editcost";
        }
        investityCostsService.save(investityCosts);
        return "redirect:/admin/investity/info?id=" + investityCosts.getInvestity().getId();
    }

    @GetMapping(path = "admin/investity/info/deletecost")
    @Secured("ROLE_ADMIN")
    String deleteInvestityCosts(@RequestParam long id) {
        long redirectId = investityCostsService.findCosts(id).orElseThrow(
                () -> new EntityNotFoundException("Could not found investity costs " + id)).getInvestity().getId();
        investityCostsService.deleteById(id);
        return "redirect:/admin/investity/info?id=" + redirectId;
    }

    @GetMapping(path = "admin/investity/info/addcost")
    @Secured("ROLE_ADMIN")
    String addInvestityCosts(Model model, @RequestParam long investityId) {
        InvestityCosts investityCosts = new InvestityCosts();
        investityCosts.setInvestity(investityService.findById(investityId)
                .orElseThrow(() -> new EntityNotFoundException("Could not found investity " + investityId)));
        model.addAttribute("investityCosts", investityCosts);
        return "admin/investity/info/addcost";
    }

    @PostMapping(path = "admin/investity/info/addcost")
    @Secured("ROLE_ADMIN")
    String addInvestityCosts(@Valid InvestityCosts investityCosts, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/investity/info/addcost";
        }
        investityCostsService.save(investityCosts);
        return "redirect:/admin/investity/info?id=" + investityCosts.getInvestity().getId();
    }

    @GetMapping(path = "admin/investity/info/showuserinfo")
    @Secured("ROLE_ADMIN")
    String showUserInfoInInvestity(@RequestParam long userid, @RequestParam long investityid, Model model) {
        List<WorkingTime> workingTime = workingTimeService.findByInvestity_IdAndUser_Id(investityid, userid);
        model.addAttribute("workingTime", workingTime);
        model.addAttribute("user", userService.findByUserId(userid)
                .orElseThrow(() -> new EntityNotFoundException("Could not found user " + userid)));
        model.addAttribute("investity", investityService.findById(investityid)
                .orElseThrow(() -> new EntityNotFoundException("Could not found investity " + investityid)));
        return "admin/investity/info/showuserinfo";
    }

    @ModelAttribute("suppliers")
    Collection<Supplier> findAllSuppliers() {
        return supplierService.findAll();
    }

    @ModelAttribute("dayOfWeekMap")
    Map<DayOfWeek, String> changeDayOfWeekNameToPolish() {
        Map<DayOfWeek, String> map = new HashMap<>();
        map.put(DayOfWeek.MONDAY, "Poniedziałek");
        map.put(DayOfWeek.TUESDAY, "Wtorek");
        map.put(DayOfWeek.WEDNESDAY, "Środa");
        map.put(DayOfWeek.THURSDAY, "Czwartek");
        map.put(DayOfWeek.FRIDAY, "Piątek");
        map.put(DayOfWeek.SATURDAY, "Sobota");
        map.put(DayOfWeek.SUNDAY, "Niedziela");
        return map;
    }

    @ModelAttribute("monthMap")
    Map<Month, String> changeMonthNameToPolish() {
        Map<Month, String> map = new HashMap<>();
        map.put(Month.JANUARY, "Styczeń");
        map.put(Month.FEBRUARY, "Luty");
        map.put(Month.MARCH, "Marzec");
        map.put(Month.APRIL, "Kwiecień");
        map.put(Month.MAY, "Maj");
        map.put(Month.JUNE, "Czerwiec");
        map.put(Month.JULY, "Lipiec");
        map.put(Month.AUGUST, "Sierpień");
        map.put(Month.SEPTEMBER, "Wrzesień");
        map.put(Month.OCTOBER, "Październik");
        map.put(Month.NOVEMBER, "Listopad");
        map.put(Month.DECEMBER, "Grudzień");
        return map;
    }
}
