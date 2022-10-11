package brain.controller;

import brain.domain.User;
import brain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userService.findAll());

        return "userList";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public String userSave( @RequestParam String name,
                            @RequestParam Map<String, String> form,
                            @RequestParam("userId") User user) {

        System.out.println("user: " + user);

        return "redirect:/userList";
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{userId}")
    public String userEditForm(@PathVariable("userId") User user, Model model) {
        System.out.println("user in userEditForm = " + user);

        model.addAttribute("user", user);
        model.addAttribute(model);

        return "userEdit";
    }

    @GetMapping("/profile")
    public String getProfile(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute(user);

        return "profile";
    }

    @PostMapping("/profile")
    public String changeProfile(User user) {

        System.out.println("user: " + user);

        return "redirect:/user/profile";
    }


}
