package brain.service;

import brain.domain.Note;
import brain.repos.NoteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return  noteRepo.findByAuthorId(id);
    }
}
