package brain.service;

import brain.domain.Note;
import brain.domain.User;
import brain.domain.dto.NoteDto;
import brain.repos.NoteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class NoteService {
    @Autowired
    NoteRepo noteRepo;

    public List<Note> findAll() { return noteRepo.findAll(); }
    public Page<Note> findByAuthor(Pageable pageable, User user) {
        return noteRepo.findByAuthor(pageable, user);
    }
    public void save(Note note) {
        noteRepo.save(note);
    }

    public Page<NoteDto> findPaginated(Pageable pageable, User user) {
        List<NoteDto> notesList = noteRepo.findAll(user);

        int pageSize = pageable.getPageSize(); //число элементов на странице
        int currentPage = pageable.getPageNumber(); //номер текущей страницы
        int startItem = pageSize * currentPage; //
        List<NoteDto> list;

        System.out.println("pageSize: " + pageSize);
        System.out.println("currentPage: " + currentPage);
        System.out.println("start item: " + startItem);
        System.out.println("notes list.size(): " + notesList.size());

        if (notesList.size() < startItem) {
            list = Collections.emptyList();
        } else {
            list = notesList.subList(startItem,
                    Math.min(startItem + pageSize, notesList.size()));
        }

        //класс помогает отфильтровать список книг с разбивкой по страницам
        return new PageImpl<NoteDto>(list,
                PageRequest.of(currentPage, pageSize), notesList.size());
    }


    /**
     * вместо метода используется синтаксис таймлифа
     * @deprecated
     */
    public List<Note> getNotesByAuthorId(Long id) {
        List<Note> currentNotes = new ArrayList<>();

        System.out.println("Ждем лист в getNotesByAuthorId(noteService)");
        List<Note> allNotes = noteRepo.findAll();
        for (var note: allNotes) {
            if (note.getAuthor().getId() == id) {
                currentNotes.add(note);
            }
        }

        if (currentNotes.isEmpty()) {
            System.out.println("Лист CurrentNotes is empty");
        }

        return currentNotes;
    }

}
