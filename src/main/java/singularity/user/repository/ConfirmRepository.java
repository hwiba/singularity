package singularity.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import singularity.user.domain.Confirm;

public interface ConfirmRepository extends JpaRepository<Confirm, Long>{
	Confirm findOneBySigningKey(String signingKey);
}