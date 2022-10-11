package brain.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class MainController {

    @GetMapping("/")
    public String greeting() {
        return "greeting.html";
    }

    @GetMapping("/main")
    public String mainPage(){
        return "main";
    }

    /*@PostMapping("/")
    public String addUser(@AuthenticationPrincipal User user) {

    }*/

}
