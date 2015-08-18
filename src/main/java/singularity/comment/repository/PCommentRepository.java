package singularity.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import singularity.comment.domain.PComment;

/**
 * Created by scala on 2015. 8. 19..
 */
public interface PCommentRepository extends JpaRepository<PComment, Long> {

}
