package pl.coderslab.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.entity.CurrentUser;
import pl.coderslab.entity.Investity;
import pl.coderslab.entity.User;
import pl.coderslab.entity.WorkingTime;
import pl.coderslab.exception.WorkingTimeNotFoundException;
import pl.coderslab.service.InvestityService;
import pl.coderslab.service.UserService;
import pl.coderslab.service.WorkingTimeService;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class WorkingTimeController {
    private final WorkingTimeService workingTimeService;
    private final InvestityService investityService;
    private final UserService userService;

    public WorkingTimeController(WorkingTimeService workingTimeService, InvestityService investityService, UserService userService) {
        this.workingTimeService = workingTimeService;
        this.investityService = investityService;
        this.userService = userService;
    }

    @Secured("ROLE_USER")
    @GetMapping (path = "user/workingtime/add")
    String addNewWorkingTime(Model model, @AuthenticationPrincipal CurrentUser currentUser){
        User user = currentUser.getUser();
        WorkingTime workingTime = new WorkingTime();
        workingTime.setLocalDate(LocalDate.now());
        workingTime.setNumberOfHours(10);
        workingTime.setInvestity(workingTimeService.findLatestInvestity(user.getId()));
        model.addAttribute("workingTime", workingTime);
        return "user/workingtime/add";
    }

    @PostMapping (path = "user/workingtime/add")
    @Secured("ROLE_USER")
    String addNewWorkingTime(@ModelAttribute WorkingTime workingTime,
                             @AuthenticationPrincipal CurrentUser currentUser){
        User user = currentUser.getUser();
        workingTime.setUser(user);
        workingTime.setSalaryPerHours(user.getSalaryPerHours());
        workingTimeService.addNewWorkingTime(workingTime);
        return "user/dashboard";
    }

    @GetMapping (path = "user/workingtime/all")
    @Secured("ROLE_USER")
    String showAllWorkingTime(Model model, @AuthenticationPrincipal CurrentUser currentUser){
        User user = currentUser.getUser();
        List<WorkingTime> allworkingtime = workingTimeService.findAllByUser_Id(user.getId());
        model.addAttribute("allworkingtime", allworkingtime);
        return "user/workingtime/all";
    }

    @GetMapping (path = "user/workingtime/edit")
    @Secured("ROLE_USER")
    String editWorkingTime(@RequestParam long id, Model model){
        Optional<WorkingTime> workingTime = workingTimeService.findById(id);
        model.addAttribute("workingTime", workingTime.orElseThrow(()-> new WorkingTimeNotFoundException(id)));
        return "user/workingtime/edit";
    }

    @PostMapping (path = "user/workingtime/edit")
    @Secured("ROLE_USER")
    String editWorkingTime(@ModelAttribute WorkingTime workingTime,
                           @AuthenticationPrincipal CurrentUser currentUser){
        User user = currentUser.getUser();
        workingTime.setUser(user);
        workingTimeService.update(workingTime);
        return "user/dashboard";
    }

    @GetMapping(path = "user/workingtime/delete")
    @Secured("ROLE_USER")
    String deleteWorkingTime(@RequestParam long id){
        Optional<WorkingTime> workingTime = workingTimeService.findById(id);
        workingTimeService.deleteById(workingTime.orElseThrow(()-> new WorkingTimeNotFoundException(id)).getId());
        return "redirect:/user/workingtime/all";
    }

    @GetMapping(path = "admin/workingtime/all")
    @Secured("ROLE_ADMIN")
    String showWorkingTimeSummary(Model model){
        LocalDate localDate = LocalDate.now();
        List<User> users = userService.findAllUsers();
        Map<Long,Integer> allHours = workingTimeService
                .getAllHoursInMonthAndYear(localDate.getMonth(),localDate.getYear());
        Map<Long,Integer> allCosts = workingTimeService
                .getAllCostsInMonthAndYear(localDate.getMonth(), localDate.getYear());

        model.addAttribute("month", localDate.getMonth());
        model.addAttribute("year", localDate.getYear());
        model.addAttribute("users",users);
        model.addAttribute("allHours", allHours);
        model.addAttribute("allCosts", allCosts);
        return "admin/workingtime/all";
    }

    @PostMapping(path = "admin/workingtime/all")
    @Secured("ROLE_ADMIN")
    String showWorkingTimeSummary(@RequestParam String date, Model model){
        YearMonth yearMonth = YearMonth.parse(date, DateTimeFormatter.ofPattern("yyyy-MM"));
        Month month = yearMonth.getMonth();
        int year = yearMonth.getYear();
        List<User> users = userService.findAllUsers();
        Map<Long,Integer> allHours = workingTimeService
                .getAllHoursInMonthAndYear(month,year);
        Map<Long,Integer> allCosts = workingTimeService
                .getAllCostsInMonthAndYear(month,year);

        model.addAttribute("month",month);
        model.addAttribute("year",year);
        model.addAttribute("users",users);
        model.addAttribute("allHours", allHours);
        model.addAttribute("allCosts", allCosts);
        return "admin/workingtime/all";
    }



    @ModelAttribute("investity")
    Collection<Investity> findAllInvesitites(){
        return investityService.findAll();
    }

//    @ModelAttribute("user")
//    Collection<User> findAllUsers(){
//        return userService.findAll();
//    }
}
