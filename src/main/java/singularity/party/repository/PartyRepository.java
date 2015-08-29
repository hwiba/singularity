package singularity.party.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import singularity.party.domain.Party;
import singularity.user.domain.User;

public interface PartyRepository extends JpaRepository<Party, Long>{
	
	Party findOneByAdmin(User user);
	
}