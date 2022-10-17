package brain.controller;

import brain.domain.Note;
import brain.domain.User;
import brain.service.NoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@Slf4j
public class NoteController {
    @Autowired
    private NoteService noteService;

    @ModelAttribute(name = "note")
    public Note note() {
        return new Note();
    }

    //добавляю список в модель таким образом потому, что когда выводятся ошибки -- список заметок перестает отображаться
    @ModelAttribute(name = "notesList")
    public List<Note> notes() { return noteService.findAll();}

    @GetMapping("/notes")
    public String getNotes(Model model)
    {
        //model.addAttribute("notesList", noteService.findAll());

        return "notes";
    }

    @PostMapping("/notes")
    public String addNote(@AuthenticationPrincipal User user,
                          @Valid Note note,
                          BindingResult bindingResult,
                          Model model) {
        System.out.println("note: " + note);
        System.out.println("user: " + user);
        if (bindingResult.hasErrors()) {
            return "notes";
        }

        note.setAuthor(user);
        noteService.save(note);

        return "redirect:/notes";
    }

}
