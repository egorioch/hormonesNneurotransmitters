package brain.controller;

import brain.domain.User;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.io.FileInputStream;

@Controller
public class HomeController {

    @GetMapping("/")
    public String getGreeting() {
        return "greeting";
    }

    @GetMapping("/home")
    public String getHome(@AuthenticationPrincipal User user,
                          Model model) {
        model.addAttribute("user", user);

        return "home";
    }
}

