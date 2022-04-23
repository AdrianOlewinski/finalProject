package pl.coderslab.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.entity.User;
import pl.coderslab.service.UserService;

import java.util.List;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(path = "admin/all")
    String showAllUsers(Model model){
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "admin/all";
    }

    @Secured("ROLE_USER")
    @GetMapping(path = "user/test")
    @ResponseBody
    String testUser(){
        return "user/test";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(path = "admin/test")
    @ResponseBody
    String testAdmin(){
        return "admin/test";
    }


}
