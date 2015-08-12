package singularity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import singularity.domain.User;

public interface UserRepository extends JpaRepository<User, String> {
	
}