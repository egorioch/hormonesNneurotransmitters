package brain.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.*;

@Data
@Entity
public class Hormone {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    //@NotBlank(message="Название гормона не может быть empty")
    @NotBlank(message = "Hormone cannot be empty!")
    @Size(min=5, max=30, message = "name should be between 5 and 30 characters")
    private String hormoneName;

    @NotBlank(message="Feeling cannot be empty")
    private String feeling;
}
