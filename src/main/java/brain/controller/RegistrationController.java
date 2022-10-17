package brain.controller;

import brain.domain.User;
import brain.domain.dto.CaptchaResponseDto;
import brain.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@Controller
@Slf4j
public class RegistrationController {
    private final static String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @ModelAttribute
    public User user() { return new User(); }

    @Value("${recaptcha.secret}")
    private String secret;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/registration")
    public String addUser(@RequestParam("g-recaptcha-response") String captchaResponse,
                          @RequestParam("passwordConfirm") String passwordConfirm,
                          @Valid User user,
                          BindingResult bindingResult,
                          Model model) {
        System.out.println("user(in reg): " + user);

        String url = String.format(CAPTCHA_URL, secret, captchaResponse);
        //responce == принятый ответ от юзера, <?>, forGoogleApiClass.class
        CaptchaResponseDto response = restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);

        if(!response.isSuccess()) {
            model.addAttribute("captchaError", "Fill captcha!");
        }

        if (bindingResult.hasErrors()) {
            System.out.println("Произошли ошибочки!");

            return "registration";
        } else {
            System.out.println("bindingResult ерунда какая-то");
        }

        if(!user.getPassword().equals(passwordConfirm)) {
            System.out.println("Пароли не совпадают");
            model.addAttribute("notConfirmError", "Password and password confirmation must be equal!");
            return "registration";
        }

        if (!userService.addUser(user)) {
            System.out.println("пользователь существует");
            model.addAttribute("userExistsError", "User exists!");
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
