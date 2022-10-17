package brain.controller;

import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.io.FileInputStream;

@Controller
public class HomeController {

    @GetMapping("/")
    public String getHome() {
        return "greeting";
    }

    @GetMapping("/home")
    public String getGreeting()
    { return "home"; }
}
