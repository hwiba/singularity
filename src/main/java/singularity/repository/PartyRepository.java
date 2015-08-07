package singularity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import singularity.domain.Party;
import singularity.domain.User;

public interface PartyRepository extends JpaRepository<Party, String>{
	
	List<Party> findAllByUsers(User user);

}