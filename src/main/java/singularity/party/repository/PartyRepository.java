package singularity.party.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import singularity.party.domain.Party;

public interface PartyRepository extends JpaRepository<Party, Long>{
	
}