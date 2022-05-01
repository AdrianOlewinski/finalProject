package pl.coderslab.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.entity.CurrentUser;
import pl.coderslab.entity.User;
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
        return "user/newDb";
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
        String userEditData(@ModelAttribute User user){
        userService.updateByUser(user);
        return "redirect:/user/dashboard";
    }

    @Secured("ROLE_USER")
    @GetMapping(path = "user/editpassword")
    String userEditPassword(){
        return "user/edit/editpassword";
    }

    @Secured("ROLE_USER")
    @PostMapping(path = "user/editpassword")
    String userEditPassword(@AuthenticationPrincipal CurrentUser currentUser, @RequestParam String oldPass,
                            @RequestParam String newPass1, @RequestParam String newPass2){
        User user = currentUser.getUser();
        userService.changePassword(user,oldPass,newPass1,newPass2);
        return "redirect:/user/dashboard";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(path="admin/user/allusers")
    String showAllUsers(Model model){
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "admin/user/allusers";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(path="admin/user/alladmins")
    String showAllAdmins(Model model){
        List<User> users = userService.findAllAdmins();
        model.addAttribute("admins", users);
        return "admin/user/alladmins";
    }

    @GetMapping(path="admin/user/add")
    @Secured("ROLE_ADMIN")
    String addNewUser(Model model){
        User user = new User();
        model.addAttribute("user",user);
        return "admin/user/add";
    }
    @PostMapping(path="admin/user/add")
    @Secured("ROLE_ADMIN")
    String addNewUser(@ModelAttribute User user){
        userService.addNewUser(user);
        return "redirect:/admin/user/allusers";
    }
    @GetMapping(path="admin/user/edit")
    @Secured("ROLE_ADMIN")
    String editUserByAdmin(Model model, @RequestParam long id){
        Optional <User> user= userService.findByUserId(id);
        model.addAttribute("user",user.get());
        return "admin/user/edit";
    }
    @PostMapping(path="admin/user/edit")
    @Secured("ROLE_ADMIN")
    String editUserByAdmin(@ModelAttribute User user){
        userService.editUserByAdmin(user);
        return "redirect:/admin/user/allusers";
    }

    @GetMapping(path="admin/user/delete")
    @Secured("ROLE_ADMIN")
    String deleteUserByAdmin(@RequestParam long id){
        userService.deleteUserById(id);
        return "redirect:/admin/user/allusers";
    }


}
