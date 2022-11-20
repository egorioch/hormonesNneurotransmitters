package brain.repos;

import brain.domain.Note;
import brain.domain.User;
import brain.domain.dto.NoteDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoteRepo extends JpaRepository<Note, Long> {
    Page<Note> findByTag(String tag, Pageable pageable);

    @Query("select m from Note m where m.author = :author")
    Page<Note> findByAuthor(Pageable pageable, @Param("author") User author);

    //Page<Note> findAll(Pageable pageable);


    @Query(
            "select new brain.domain.dto.NoteDto(" +
                    "   m, " +
                    "   count(ml), " +
                    "   sum(case when ml = :user then 1 else 0 end) > 0" +
                    "   )" +
                    "from Note m left join m.likes ml " +
                    "group by m ")
    List<NoteDto> findAll(@Param("user") User user);
}
