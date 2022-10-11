package brain.controller;

import brain.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/login")
public class LoginController {

    //с ним остается проблема. Почему выкидывает ошибку при логинге - непонятно.
    @GetMapping
    public String getLogin(@RequestParam(value="error", required = false) boolean loginError,
                           Model model) {
        if (loginError) {
            model.addAttribute("message", "Error with login!");
        }
        //model.addAttribute("message", "OLA");
        return "login";
    }

    @PostMapping
    public String enterInAccount(Model model, User user) {

        System.out.println(user);

        return "home";
    }


}
