package pl.coderslab.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.entity.CurrentUser;
import pl.coderslab.entity.Password;
import pl.coderslab.entity.User;
import pl.coderslab.exception.EntityNotFoundException;
import pl.coderslab.service.UserService;

import javax.validation.Valid;
import java.util.*;

@Controller
public class UserController {
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserController(UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(path = "admin/dashboard")
    String adminDashboard() {
        return "redirect:/admin/investity/all";
    }

    @Secured("ROLE_USER")
    @GetMapping(path = "user/dashboard")
    String userDashboard() {
        return "redirect:/user/workingtime/add";
    }

    @Secured("ROLE_USER")
    @GetMapping(path = "user/editdata")
    String userEditData(@AuthenticationPrincipal CurrentUser currentUser, Model model) {
        Long id = currentUser.getUser().getId();
        Optional<User> user = userService.findByUserId(id);
        model.addAttribute("user", user.get());
        return "user/edit/editdata";
    }

    @Secured("ROLE_USER")
    @PostMapping(path = "user/editdata")
    String userEditData(@Valid User user, BindingResult result) {
        if (userService.isUsernameTaken(user)) {
            result.addError(new FieldError("user", "username", "Login jest już zajęty!"));
        }

        if (result.hasErrors()) {
            return "user/edit/editdata";
        }
        userService.update(user);
        return "redirect:/user/dashboard";
    }

    @Secured("ROLE_USER")
    @GetMapping(path = "user/editpassword")
    String userEditPassword(Model model) {
        model.addAttribute("password", new Password());
        return "user/edit/editpassword";
    }

    @Secured("ROLE_USER")
    @PostMapping(path = "user/editpassword")
    String userEditPassword(@AuthenticationPrincipal CurrentUser currentUser,
                            @Valid Password password, BindingResult result) {

        User user = currentUser.getUser();
        if (!passwordEncoder.matches(password.getOldPassword(), user.getPassword())) {
            result.addError(new FieldError("password", "oldPassword"
                    , "Nie zgodne stare hasło!"));
        }
        if (!password.getNewPassword1().equals(password.getNewPassword2())) {
            result.addError(new FieldError("password", "newPassword2"
                    , "Nowe hasło i powtórzone hasło nie są zgodne!"));
        }
        if (result.hasErrors()) {
            return "user/edit/editpassword";
        }
        userService.changePassword(user, password.getNewPassword1());
        return "redirect:/user/dashboard";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(path = "admin/user/allusers")
    String showAllUsers(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "admin/user/allusers";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping(path = "admin/user/alladmins")
    String showAllAdmins(Model model) {
        List<User> users = userService.findAllAdmins();
        model.addAttribute("admins", users);
        return "admin/user/alladmins";
    }
    @Secured("ROLE_ADMIN")
    @GetMapping(path = "admin/user/allinactiveusers")
    String showInActiveUsers(Model model) {
        List<User> users = userService.findInactiveUsers();
        model.addAttribute("users", users);
        return "admin/user/allinactiveusers";
    }

    @GetMapping(path = "admin/user/add")
    @Secured("ROLE_ADMIN")
    String addNewUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "admin/user/add";
    }

    @PostMapping(path = "admin/user/add")
    @Secured("ROLE_ADMIN")
    String addNewUser(@Valid User user, BindingResult result) {
        if (userService.isUsernameTaken(user)) {
            result.addError(new FieldError("user", "username", "Login jest już zajęty!"));
        }

        if (result.hasErrors()) {
            return "admin/user/add";
        }
        userService.addNewUser(user);
        return "redirect:/admin/user/allusers";
    }

    @GetMapping(path = "admin/user/edit")
    @Secured("ROLE_ADMIN")
    String editUserByAdmin(Model model, @RequestParam long id) {
        Optional<User> user = userService.findByUserId(id);
        model.addAttribute("user", user.orElseThrow(()->new EntityNotFoundException("Could not found user " + id)));
        return "admin/user/edit";
    }

    @PostMapping(path = "admin/user/edit")
    @Secured("ROLE_ADMIN")
    String editUserByAdmin(@Valid User user, BindingResult result) {
        if (userService.isUsernameTaken(user)) {
            result.addError(new FieldError("user", "username", "Login jest już zajęty!"));
        }

        if (result.hasErrors()) {
            return "admin/user/edit";
        }
        userService.update(user);
        return "redirect:/admin/user/allusers";
    }

    @GetMapping(path = "admin/user/delete")
    @Secured("ROLE_ADMIN")
    String deleteUserByAdmin(@RequestParam long id) {
        userService.deleteUserById(id);
        return "redirect:/admin/user/allusers";
    }

    @GetMapping(path = "admin/user/resetpassword")
    @Secured("ROLE_ADMIN")
    String resetPassword(@RequestParam long id, Model model){
        Password password = new Password();
        password.setOldPassword("password");
        password.setNewPassword2("password");
        model.addAttribute("user",userService.findByUserId(id)
                .orElseThrow(()->new EntityNotFoundException("Could not find user id= "+id)));
        model.addAttribute("password",password);
        return "admin/user/resetpassword";
    }

    @PostMapping(path = "admin/user/resetpassword")
    @Secured("ROLE_ADMIN")
    String resetPassword(@RequestParam long id, @Valid Password password, BindingResult result){
        if (result.hasErrors()) {
            return "admin/user/resetpassword";
        }
        userService.changePassword(userService.findByUserId(id).orElseThrow
                (()->new EntityNotFoundException("Could not find user id= "+id)),password.getNewPassword1());
        return "redirect:/admin/user/allusers";
    }

    @GetMapping(path = "admin/user/changerole")
    @Secured("ROLE_ADMIN")
    String changeRole(@RequestParam long id){
        userService.changeRole(id);
        return "redirect:/admin/user/allusers";
    }

    @GetMapping(path = "admin/error")
    @Secured("ROLE_ADMIN")
    String error(@RequestParam int id, Model model){
        Map<Integer, String> errors = new HashMap<>();
        errors.put(1,"Nie możesz usunąć inwestycji, ponieważ do niej są przypisani pracownicy albo dostawcy!");
        errors.put(2,"Nie możesz usunąć dostawcy, przypisany jest on do jakiejś inwestycji!");
        String error = errors.get(id);
        System.out.println(error);
        model.addAttribute("error",error);
        return "admin/error";
    }


}
