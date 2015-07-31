package singularity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import singularity.domain.Note;
import singularity.domain.PComment;
import singularity.domain.User;

public interface PCommentRepository extends JpaRepository<PComment, String>{
	
	List<PComment> findAllByUser(User user);
	
	List<PComment> findAllByNote(Note note);
}
