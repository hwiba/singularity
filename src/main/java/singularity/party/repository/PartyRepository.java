package singularity.party.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyRepository extends JpaRepository<Party, String>{
	
	List<Party> findAllByUsers(User user);

}