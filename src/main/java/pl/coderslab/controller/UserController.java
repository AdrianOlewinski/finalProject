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
import pl.coderslab.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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
        return "admin/newDb";
    }

    @Secured("ROLE_USER")
    @GetMapping(path = "user/dashboard")
    String userDashboard() {
        return "user/newDb";
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
        userService.updateByUser(user);
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

    @GetMapping(path = "admin/user/add")
    @Secured("ROLE_ADMIN")
    String addNewUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "admin/user/add";
    }

    @PostMapping(path = "admin/user/add")
    @Secured("ROLE_ADMIN")
    String addNewUser(@ModelAttribute User user) {
        userService.addNewUser(user);
        return "redirect:/admin/user/allusers";
    }

    @GetMapping(path = "admin/user/edit")
    @Secured("ROLE_ADMIN")
    String editUserByAdmin(Model model, @RequestParam long id) {
        Optional<User> user = userService.findByUserId(id);
        model.addAttribute("user", user.get());
        return "admin/user/edit";
    }

    @PostMapping(path = "admin/user/edit")
    @Secured("ROLE_ADMIN")
    String editUserByAdmin(@ModelAttribute User user) {
        userService.editUserByAdmin(user);
        return "redirect:/admin/user/allusers";
    }

    @GetMapping(path = "admin/user/delete")
    @Secured("ROLE_ADMIN")
    String deleteUserByAdmin(@RequestParam long id) {
        userService.deleteUserById(id);
        return "redirect:/admin/user/allusers";
    }


}
