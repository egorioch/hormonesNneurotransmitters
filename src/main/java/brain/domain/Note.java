package brain.domain;

import brain.domain.util.NoteHelper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "note")
@Getter
@Setter
@RequiredArgsConstructor
public class Note implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    @NotBlank(message = "Fill the tag!")
    @Length(max = 255, message = "message too long(you can enter 1/4kB symbols)")
    private String tag;

    @NotBlank(message = "Please enter a message in the textarea.")
    @Length(max = 2048, message = "message too long(you can enter 2kB symbols)")
    private String text;

    //метод нужно вынести в отдельный хелпер, чтобы можно было использовать
    //его в noteDTO и изменять, не трогая класс Note
    public String getAuthorName() {
        return NoteHelper.getAuthorName(author);
    }

    @ManyToMany
    @JoinTable(
            name = "note_likes",
            joinColumns = {@JoinColumn(name = "note_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private Set<User> likes = new HashSet<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Note note = (Note) o;
        return Objects.equals(id, note.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
