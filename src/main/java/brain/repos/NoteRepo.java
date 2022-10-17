package brain.repos;

import brain.domain.Note;
import brain.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepo extends JpaRepository<Note, Long> {
    Note findByTag(String tag);
}
