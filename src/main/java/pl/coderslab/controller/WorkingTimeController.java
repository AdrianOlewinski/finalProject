package pl.coderslab.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.coderslab.entity.Investity;
import pl.coderslab.entity.User;
import pl.coderslab.entity.WorkingTime;
import pl.coderslab.service.InvestityService;
import pl.coderslab.service.UserService;
import pl.coderslab.service.WorkingTimeService;

import java.util.Collection;

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
    @GetMapping (path = "user/workingtime")
    String addNewWorkingTime(Model model){
        WorkingTime workingTime = new WorkingTime();
        workingTime.setId(1);
        model.addAttribute("workingTime", workingTime);
        return "workingtime/add";
    }

    @PostMapping (path = "user/workingtime")
    @Secured("ROLE_USER")
    @ResponseBody
    String addNewWorkingTime(@ModelAttribute WorkingTime workingTime, Model model){
        workingTimeService.addNewWorkingTime(workingTime);
        return "gotowe";
    }

    @ModelAttribute("investity")
    Collection<Investity> findAllInvesitites(){
        return investityService.findAll();
    }

    @ModelAttribute("user")
    Collection<User> findAllUsers(){
        return userService.findAll();
    }
}
