package singularity.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import singularity.comment.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{
	
	//List<Comment> findAllByUser(User user);
	
}