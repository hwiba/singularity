package stag.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import stag.domain.User;

public interface UserRepository extends JpaRepository<User, String>{
	
}