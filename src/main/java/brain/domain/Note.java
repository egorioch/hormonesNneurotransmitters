package brain.domain;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "note")
@Data
public class Note {
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


}
