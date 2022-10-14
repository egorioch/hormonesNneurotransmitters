package brain.controller;

import brain.domain.User;
import brain.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Controller
@Slf4j
public class RegistrationController {
    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @ModelAttribute
    public User user() { return new User(); }

    @PostMapping("/registration")
    public String addUser(@Valid User user,
                          BindingResult bindingResult,
                          Model model) {
        System.out.println("user(in reg): " + user);

        if (bindingResult.hasErrors()) {
            System.out.println("Произошли ошибочки!");

            return "registration";
        } else {
            System.out.println("bindingResult ерунда какая-то");
        }

        if(!user.getPassword().equals(user.getPasswordConfirm())) {
            model.addAttribute("message", "password and password confirmation must be equal");
        }

        if (!userService.addUser(user)) {
            model.addAttribute("message", "User exists!");
            return "registration";
        }

        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String isActivate(@PathVariable String code, Model model) {

        if(userService.activateUser(code)) {
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "Account is activated successfully...");

        } else {
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Code is not found!");
        }


        return "login";
    }
}
