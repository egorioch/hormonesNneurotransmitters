package brain.service;

import brain.domain.Note;
import brain.repos.NoteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NoteService {
    @Autowired
    NoteRepo noteRepo;

    public List<Note> findAll() {return noteRepo.findAll();}

    public void save(Note note) {
        noteRepo.save(note);
    }

    public List<Note> findByAuthorId(Long id) {
        return noteRepo.findByAuthorId(id);
    }

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
