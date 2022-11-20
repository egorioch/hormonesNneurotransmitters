package brain.controller;

import brain.domain.Note;
import brain.domain.User;
import brain.domain.dto.NoteDto;
import brain.repos.UserRepo;
import brain.service.NoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.IntStream;

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

    @Autowired
    private EntityManager em;


    @GetMapping("/notes")
    public String getNotes(@AuthenticationPrincipal User user,
                           @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                           @RequestParam("page") Optional<Integer> page,
                           @RequestParam("size") Optional<Integer> size,
                           Model model) {
        System.out.println("auth user in getNotes == " + user);
        model.addAttribute("user", user);

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        // pageRequest == pageable
        // of(индекс страницы, размер возвращаемой страницы)
        Page<NoteDto> notesPage = noteService.findPaginated(PageRequest.of(currentPage - 1, pageSize), user);


        //общее число страниц, определяемое по входным параметрам
        int totalPages = notesPage.getTotalPages();
        //создаем массив int, чтобы перебирать по страницам
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed().toList();
            model.addAttribute("pageNumbers", pageNumbers);
        }

        System.out.println("notesPage number: " + notesPage.getNumber());
        System.out.println("totalPage: " +  notesPage.getTotalPages());

        model.addAttribute("notesPage", notesPage);

        return "notes";
    }

    @ModelAttribute("pageSize")
    private List<Integer> setSizes(Model model) {
        int[] sizes = new int[] {5, 10, 15, 20};
        return Arrays.stream(sizes).boxed().toList();
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
        //user.getNotes().add(note);

        return "redirect:/notes";
    }


    //переход по кнопке edit
    //вообще, можно сделать всю проверку здесь: если authUser == userEditId --разрешить редактирование
    @GetMapping("/user-notes/{userChannel}")
    public String updateNote(@AuthenticationPrincipal User user,
                             @PathVariable("userChannel") User userChannel,
                             @PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable,
                             Model model) {

        //System.out.println("totalPages(from HQL) = " + noteService.findByAuthor(pageable, user).getTotalPages());


        model.addAttribute("user", user);
        model.addAttribute("userChannel", userChannel);

        List<Note> notes = userChannel.getNotes();//noteService.getNotesByAuthorId(userChannel.getId());

        //является ли текущий пользователь хозяином страницы
        boolean isCurrentUser = user.getId().equals(userChannel.getId());
        //Текущий пользователь -- подписчик канала
        boolean isSubscriber = userChannel.getSubscribers().contains(user);

        System.out.println("Число подписок: " + userChannel.getSubscriptions().size());
        System.out.println("Число подписчиков: " + userChannel.getSubscribers().size());

        model.addAttribute("notesUserList", notes);
        model.addAttribute("subscriptionsCount", userChannel.getSubscriptions().size());
        model.addAttribute("subscribersCount", userChannel.getSubscribers().size());
        model.addAttribute("isCurrentUser", isCurrentUser);
        model.addAttribute("isSubscriber", isSubscriber);

        return "user-notes";
    }

    @GetMapping("/notes/{note}/like")
    public String like(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Note note,
            //параметры, который можно будет пробросить с помощью редиректа
            RedirectAttributes redirectAttributes,
            //отсюда мы будем получать страницу, на которую будем редиректиться
            @RequestHeader(required = false) String referer
    ) {
        System.out.println("\n\n\n\n\n current noteId=" + note.getId());
        Set<User> likes = note.getLikes();

        if (likes.contains(currentUser)) {
            likes.remove(currentUser);
        } else {
            likes.add(currentUser);
        }
        for (var like: likes) {
            System.out.println("like: " + like.getUsername());
        }

        System.out.println("referer: " + referer);
        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();

        //здесь мы получаем текущее положение по пагинации, разные записи, типа фильтров, ну и другие параметры
        components.getQueryParams()
                .entrySet()
                .forEach(pair -> redirectAttributes.addAttribute(pair.getKey(), pair.getValue()));

        System.out.println("Вывод пар RedirectAttributes: ");
        components.getQueryParams()
                .entrySet()
                .forEach(pair -> System.out.println(pair.getKey() + pair.getValue()));

        return "redirect:" + components.getPath();
    }



}
