package singularity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import singularity.domain.Note;
import singularity.domain.PComment;
import singularity.domain.User;
import singularity.dto.out.PCommentCountByP;

public interface PCommentRepository extends JpaRepository<PComment, String>{
	
	List<PComment> findAllByUser(User user);
	
	List<PComment> findAllByNote(Note note);
	
	List<PComment> findAllByPIdAndNote(int pId, Note note);
	
	//@Query("select pId, count(1) from pcomment, user where pcomment.user_FK = user.id AND noteId = ? GROUP BY pId")
	List<PCommentCountByP> countAllByNoteByP(long noteId);
}
