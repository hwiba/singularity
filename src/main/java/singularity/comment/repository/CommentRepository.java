package singularity.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import singularity.comment.domain.Comment;
import singularity.note.domain.Note;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByNote(Note note);

    List<Comment> findAllByNoteAndPageId(Note note, Integer pageId);

}
