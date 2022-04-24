package pl.coderslab.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.entity.CurrentUser;
import pl.coderslab.entity.User;
import pl.coderslab.entity.WorkingTime;
import pl.coderslab.service.UserService;

import java.util.List;
import java.util.Optional;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(path = "admin/dashboard")
    String adminDashboard(){
        return "admin/dashboard";
    }

    @Secured("ROLE_USER")
    @GetMapping(path = "user/dashboard")
    String userDashboard(){
        return "user/dashboard";
    }

    @Secured("ROLE_USER")
    @GetMapping(path = "user/editdata")
    String userEditData(@AuthenticationPrincipal CurrentUser currentUser, Model model){
        Long id = currentUser.getUser().getId();
        Optional<User> user = userService.findByUserId(id);
        model.addAttribute("user",user.get());
        return "user/edit/editdata";
    }

    @Secured("ROLE_USER")
    @PostMapping(path = "user/editdata")
    @ResponseBody
        String userEditData(@ModelAttribute User user, @RequestParam long id, @RequestParam String username){
        return id + username;
//        userService.update(user);
//        return "redirect:/user/dashboard";
    }


}
