package brain.domain.util;

import brain.domain.User;

public abstract class NoteHelper {
    public static String getAuthorName(User author) {
        return author != null ? author.getUsername() : "<none>";
    }
}
