package brain.domain.dto;

import brain.domain.Note;
import brain.domain.User;
import brain.domain.util.NoteHelper;
import lombok.Getter;

@Getter
public class NoteDto {
    private Long id;
    private String text;
    private String tag;
    private User author;
    private Long likes;
    private Boolean meLiked;

    public NoteDto(Note note, Long likes, Boolean meLiked) {
        this.id = note.getId();
        this.text = note.getText();
        this.tag = note.getTag();
        this.author = note.getAuthor();
        this.likes = likes;
        this.meLiked = meLiked;
    }
    //выносим в отдельный метод, чтобы не использовать note-объект
    public String getAuthorName() {
        return NoteHelper.getAuthorName(author);
    }

    @Override
    public String toString() {
        return "NoteDto{" +
                "id=" + id +
                ", author=" + author +
                ", likes=" + likes +
                ", meLiked=" + meLiked +
                '}';
    }

}
