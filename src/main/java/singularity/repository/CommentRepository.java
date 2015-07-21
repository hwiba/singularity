package singularity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import singularity.domain.Comment;
import singularity.domain.Note;
import singularity.domain.User;

public interface CommentRepository extends JpaRepository<Comment, Long>{
	
	List<Comment> findAllByUser(User user);
	
	List<Comment> findAllByNote(Note note);
	
}