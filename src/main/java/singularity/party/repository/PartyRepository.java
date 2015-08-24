package singularity.party.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import singularity.party.domain.Party;
import singularity.user.domain.User;

public interface PartyRepository extends JpaRepository<Party, Long>{
	
	List<Party> findAllByMembers(User user);
	
}