package singularity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import singularity.domain.PComment;
import singularity.domain.User;

public interface PCommentRepository extends JpaRepository<PComment, String>{
	
	List<PComment> findAllByUser(User user);
	
	//@Query("select pId, count(1) from pcomment, user where pcomment.user_FK = user.id AND noteId = ? GROUP BY pId")
	//List<PCommentCountByP> countAllByNoteByP(long noteId);
}
