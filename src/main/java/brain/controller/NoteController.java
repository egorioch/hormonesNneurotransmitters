package brain.controller;

import brain.domain.Note;
import brain.domain.User;
import brain.repos.UserRepo;
import brain.service.NoteService;
import brain.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Controller
@Slf4j
public class NoteController {
    @Autowired
    private NoteService noteService;
    @Autowired
    private UserRepo userRepo;

    @ModelAttribute(name = "note")
    public Note note() {
        return new Note();
    }
    //добавляю список в модель таким образом потому, что когда выводятся ошибки -- список заметок перестает отображаться

    @ModelAttribute(name = "notesList")
    public Iterable<Note> notes() { return noteService.findAll();}

    /*@ModelAttribute(name = "user")
    public User user(@AuthenticationPrincipal User user) { return user; }*/

    @GetMapping("/notes")
    public String getNotes(Model model, @AuthenticationPrincipal User user) {
        System.out.println("auth user in getNotes == " + user);
        model.addAttribute("user", user);

        return "notes";
    }

    @PostMapping("/notes")
    public String addNote(@AuthenticationPrincipal User user,
                          @Valid Note note,
                          BindingResult bindingResult,
                          Model model) {
        note.setAuthor(user);

        if (bindingResult.hasErrors()) {
            System.out.println("Возникли ошибочки!");
            return "notes";
        }

        noteService.save(note);
        //Iterable<Note> notes = noteService.findAll();
        //model.addAttribute("notesList", notes);

        return "redirect:/notes";
    }

    /*@GetMapping("/user-notes")
    public String getMyNotes(@AuthenticationPrincipal User user) {
        System.out.println("user in user-notes: " + user);

        return "user-notes";
    }*/

    @GetMapping("/user-notes/{id}")
    public String updateNote(@AuthenticationPrincipal User user,
                             @PathVariable("id") Long id,
                             Model model) {

        model.addAttribute("user", user);
        System.out.println("user in updateNote == " + user);
        System.out.println("id == " + id);
        // 1) положить конкретное сообщение
        // 2) положить весь список, который можно отредактировать
        Set<Note> notes = user.getNotes();

        System.out.println("got notes: ");
        notes.forEach(System.out::println);

        model.addAttribute("notesList", notes);
        model.addAttribute("userChanel", user);
        /*model.addAttribute("subscriptionsCount", user.getSubscriptions().size());
        model.addAttribute("subscribersCount", user.getSubscribers().size());*/


        return "/user-notes" + "/" + id;
    }

}
