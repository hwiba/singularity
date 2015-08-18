package singularity.comment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>{
	
	List<Comment> findAllByUser(User user);
	
}