package brain.controller;

import brain.domain.Hormone;
import brain.service.HormoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/hormone")
public class HormoneController {
    @Autowired
    HormoneService hormoneService;

    @GetMapping
    public String getHormone() {
        return "hormone";
    }

    @ModelAttribute(name = "hormone")
    public Hormone hormone() { return new Hormone(); }

    @PostMapping
    public String findHormone(@Valid Hormone hormone, BindingResult errors, Model model) {
        System.out.println("posted hormone" + hormone);
        //model.addAttribute("hormone", new Hormone());

        hormoneService.outFiles(hormoneService.readFiles());

        if (errors.hasErrors()) {
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "hormone is not found!");
            System.out.println("Были найдены ошибки");

            return "hormone";
        }
        model.addAttribute("messageType", "success!");
        model.addAttribute("message", "hormone is find!");

        return "redirect:/hormone";
    }
}
