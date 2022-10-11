package brain.controller;

import brain.domain.User;
import brain.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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

    @PostMapping("/registration")
    public String addUser(User user, Map<String, Object> model) {
        System.out.println("user(in reg): " + user);
        if (!userService.addUser(user)) {
            model.put("message", "User exists!");
            return "registration";
        }

        model.put("message", "The mail with verification code has send on your email! " +
                "\nPlease, to confirm it.");
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
