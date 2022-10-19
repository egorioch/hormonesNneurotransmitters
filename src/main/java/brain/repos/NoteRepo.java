package brain.repos;

import brain.domain.Note;
import brain.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepo extends JpaRepository<Note, Long> {
    List<Note> findByTag(String tag);
    List<Note> findByAuthorId(Long author_id);

}
