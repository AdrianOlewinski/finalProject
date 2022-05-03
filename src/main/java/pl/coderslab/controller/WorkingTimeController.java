package pl.coderslab.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.entity.CurrentUser;
import pl.coderslab.entity.Investity;
import pl.coderslab.entity.User;
import pl.coderslab.entity.WorkingTime;
import pl.coderslab.exception.EntityNotFoundException;
import pl.coderslab.service.InvestityService;
import pl.coderslab.service.UserService;
import pl.coderslab.service.WorkingTimeService;

import javax.validation.Valid;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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
        workingTime.setMultiplier(1);
        workingTime.setUser(user);
        workingTime.setSalaryPerHours(user.getSalaryPerHours());
        model.addAttribute("workingTime", workingTime);
        return "user/workingtime/add";
    }

    @PostMapping (path = "user/workingtime/add")
    @Secured("ROLE_USER")
    String addNewWorkingTime(@Valid WorkingTime workingTime,BindingResult result){
        if(workingTimeService.isNumberOfHoursInOneDayBiggerThan24(workingTime)){
            result.addError(new FieldError("workingTime", "numberOfHours"
                    , "Nie może w jednym dniu być więcej godzin niż 24!!"));
        }

        if(result.hasErrors()){
            return "user/workingtime/add";
        }
        System.out.println(workingTime);
        workingTimeService.addNewWorkingTime(workingTime);
        return "redirect:/user/workingtime/all";
    }

    @GetMapping (path = "user/workingtime/all")
    @Secured("ROLE_USER")
    String showAllWorkingTime(Model model, @AuthenticationPrincipal CurrentUser currentUser){
        User user = currentUser.getUser();
        LocalDate localDate = LocalDate.now();
        List<WorkingTime> allworkingtime = workingTimeService.findAllByUser_Id(user.getId())
                .stream().filter(s->s.getLocalDate().getMonth().equals(localDate.getMonth())
                        &&s.getLocalDate().getYear()== localDate.getYear()).collect(Collectors.toList());

        model.addAttribute("sumOfHours", workingTimeService
                .sumOfAllHoursInMonthAndYear(user.getId(), localDate.getMonth(), localDate.getYear()));
        model.addAttribute("sumOfCosts", workingTimeService
                .sumOfAllCostsInMonthAndYear(user.getId(), localDate.getMonth(), localDate.getYear()));
        model.addAttribute("month", localDate.getMonth());
        model.addAttribute("year", localDate.getYear());
        model.addAttribute("allworkingtime", allworkingtime);
        return "user/workingtime/all";
    }
    @PostMapping(path = "user/workingtime/all")
    @Secured("ROLE_ADMIN")
    String showAllWorkingTime(@RequestParam String date, Model model, @AuthenticationPrincipal CurrentUser currentUser){
        YearMonth yearMonth = YearMonth.parse(date, DateTimeFormatter.ofPattern("yyyy-MM"));
        Month month = yearMonth.getMonth();
        int year = yearMonth.getYear();
        User user = currentUser.getUser();
        List<WorkingTime> allworkingtime = workingTimeService.findAllByUser_Id(user.getId())
                .stream().filter(s->s.getLocalDate().getMonth().equals(month)
                        &&s.getLocalDate().getYear()== year).collect(Collectors.toList());

        model.addAttribute("sumOfHours", workingTimeService
                .sumOfAllHoursInMonthAndYear(user.getId(), month, year));
        model.addAttribute("sumOfCosts", workingTimeService
                .sumOfAllCostsInMonthAndYear(user.getId(), month, year));
        model.addAttribute("month",month);
        model.addAttribute("year",year);
        model.addAttribute("allworkingtime", allworkingtime);
        return "user/workingtime/all";
    }

    @GetMapping (path = "user/workingtime/edit")
    @Secured("ROLE_USER")
    String editWorkingTime(@RequestParam long id, Model model
            , @AuthenticationPrincipal CurrentUser currentUser) throws IllegalAccessException{
        User user = currentUser.getUser();
        WorkingTime workingTime = workingTimeService.findById(id).orElseThrow(()->new EntityNotFoundException(id));
        if(workingTime.getUser().getId() != user.getId()){
            throw new IllegalAccessException("Brak dostępu!");
        }
        model.addAttribute("workingTime", workingTime);
        return "user/workingtime/edit";
    }

    @PostMapping (path = "user/workingtime/edit")
    @Secured("ROLE_USER")
    String editWorkingTime(@Valid WorkingTime workingTime, BindingResult result,
                           @AuthenticationPrincipal CurrentUser currentUser){
        if(workingTimeService.isNumberOfHoursInOneDayBiggerThan24(workingTime)){
            result.addError(new FieldError("workingTime", "numberOfHours"
                    , "Nie może w jednym dniu być więcej godzin niż 24!!"));
        }
        if(result.hasErrors()){
            return "user/workingtime/edit";
        }
        System.out.println(workingTime);
        workingTimeService.update(workingTime);
        return "redirect:/user/workingtime/all";
    }

    @GetMapping(path = "user/workingtime/delete")
    @Secured("ROLE_USER")
    String deleteWorkingTime(@RequestParam long id, @AuthenticationPrincipal CurrentUser currentUser)
            throws IllegalAccessException{
        User user = currentUser.getUser();
        WorkingTime workingTime = workingTimeService.findById(id).orElseThrow(()->new EntityNotFoundException(id));
        if(workingTime.getUser().getId() != user.getId()){
            throw new IllegalAccessException("Brak dostępu!");
        }
        workingTimeService.deleteById(workingTime.getId());
        return "redirect:/user/workingtime/all";
    }

    @GetMapping(path = "admin/workingtime/all")
    @Secured("ROLE_ADMIN")
    String showWorkingTimeSummary(Model model){
        LocalDate localDate = LocalDate.now();
        List<User> users = userService.findAllUsers();
        Map<Long,Double> allHoursWithMultiplierOne = workingTimeService
                .getAllHoursByUserInMonthWithMultiplierOne(localDate.getMonth(),localDate.getYear());
        Map<Long,Double> allHoursWithOtherMultiplier = workingTimeService
                .getAllHoursByUserInMonthWithOtherMultiplier(localDate.getMonth(),localDate.getYear());
        Map<Long,Double> allCosts = workingTimeService
                .getAllCostsInMonthAndYear(localDate.getMonth(), localDate.getYear());

        model.addAttribute("month", localDate.getMonth());
        model.addAttribute("year", localDate.getYear());
        model.addAttribute("users",users);
        model.addAttribute("allHoursWithMultiplierOne", allHoursWithMultiplierOne);
        model.addAttribute("allHoursWithOtherMultiplier", allHoursWithOtherMultiplier);
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
        Map<Long,Double> allHoursWithMultiplierOne = workingTimeService
                .getAllHoursByUserInMonthWithMultiplierOne(month,year);
        Map<Long,Double> allHoursWithOtherMultiplier = workingTimeService
                .getAllHoursByUserInMonthWithOtherMultiplier(month,year);
        Map<Long,Double> allCosts = workingTimeService
                .getAllCostsInMonthAndYear(month,year);

        model.addAttribute("month",month);
        model.addAttribute("year",year);
        model.addAttribute("users",users);
        model.addAttribute("allHoursWithMultiplierOne",allHoursWithMultiplierOne);
        model.addAttribute("allHoursWithOtherMultiplier",allHoursWithOtherMultiplier);
        model.addAttribute("allCosts", allCosts);
        return "admin/workingtime/all";
    }

    @GetMapping(path="admin/workingtime/info")
    @Secured("ROLE_ADMIN")
    String showUserWorkingTimeSummary (@RequestParam long id, @RequestParam Month month,
                                        @RequestParam int year, Model model){
        List<WorkingTime> workingTime = workingTimeService.findAllByUser_Id(id)
                .stream().filter(s->s.getLocalDate().getMonth().equals(month)&&s.getLocalDate().getYear()==year)
                .collect(Collectors.toList());
        model.addAttribute("workingTime",workingTime);
        model.addAttribute("user",userService.findByUserId(id).orElseThrow(()->new EntityNotFoundException(id)));
        return "admin/workingtime/info";
    }
    @GetMapping(path ="admin/workingtime/delete")
    @Secured("ROLE_ADMIN")
    String deleteWorkingTimeByAdmin (@RequestParam long id){
        Optional<WorkingTime> workingTimeOptional = workingTimeService.findById(id);
        WorkingTime workingTime = workingTimeOptional.orElseThrow(()->new EntityNotFoundException(id));
        workingTimeService.deleteById(id);
        return "redirect:/admin/workingtime/info?id="+workingTime.getUser().getId()
                +"&month="+workingTime.getLocalDate().getMonth()+"&year="+workingTime.getLocalDate().getYear();
    }
    @GetMapping(path ="admin/workingtime/edit")
    @Secured("ROLE_ADMIN")
    String editWorkingTimeByAdmin (@RequestParam long id, Model model){
        Optional<WorkingTime> workingTime = workingTimeService.findById(id);
        model.addAttribute("workingtime",
                workingTime.orElseThrow((()->new EntityNotFoundException(id))));
        return "admin/workingtime/edit";
    }
    @PostMapping(path ="admin/workingtime/edit")
    @Secured("ROLE_ADMIN")
    String editWorkingTimeByAdmin (@Valid WorkingTime workingTime, BindingResult result){
        if(workingTimeService.isNumberOfHoursInOneDayBiggerThan24(workingTime)){
            result.addError(new FieldError("workingTime", "numberOfHours"
                    , "Nie może w jednym dniu być więcej godzin niż 24!!"));
        }
        if(result.hasErrors()){
            return "admin/workingtime/edit";
        }
        workingTimeService.update(workingTime);
        return "redirect:/admin/workingtime/info?id="+workingTime.getUser().getId()
                +"&month="+workingTime.getLocalDate().getMonth()+"&year="+workingTime.getLocalDate().getYear();
    }



    @ModelAttribute("investity")
    Collection<Investity> findAllInvesitites(){
        return investityService.findAll();
    }

    @ModelAttribute("dayOfWeekMap")
    Map<DayOfWeek, String> changeDayOfWeekNameToPolish(){
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
    Map<Month, String> changeMonthNameToPolish(){
        Map<Month, String> map = new HashMap<>();
        map.put(Month.JANUARY, "Styczeń");
        map.put(Month.FEBRUARY, "Luty");
        map.put(Month.MARCH , "Marzec");
        map.put(Month.APRIL , "Kwiecień");
        map.put(Month.MAY , "Maj");
        map.put(Month.JUNE , "Czerwiec");
        map.put(Month.JULY , "Lipiec");
        map.put(Month.AUGUST , "Sierpień");
        map.put(Month.SEPTEMBER , "Wrzesień");
        map.put(Month.OCTOBER , "Październik");
        map.put(Month.NOVEMBER , "Listopad");
        map.put(Month.DECEMBER , "Grudzień");
        return map;
    }

//    @ModelAttribute("user")
//    Collection<User> findAllUsers(){
//        return userService.findAll();
//    }
}
