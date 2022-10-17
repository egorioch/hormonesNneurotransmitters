package brain.domain;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Data
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    @NotBlank(message = "Fill the tag!")
    @Length(max = 2048, message = "message too long(you can enter 1/4kB symbols)")
    private String tag;

    @NotBlank(message = "Fill the message!")
    @Length(max = 2048, message = "message too long(you can enter 2kB symbols)")
    private String text;



}
