package singularity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import singularity.domain.Confirm;

public interface ConfirmRepository extends JpaRepository<Confirm, Long>{
	Confirm findOneBySigningKey(String signingKey);
}