package singularity.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import singularity.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findOneByEmail(String email);
	
}