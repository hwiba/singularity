package stag.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import stag.domain.Confirm;

public interface ConfirmRepository extends JpaRepository<Confirm, Long>{
	Confirm findOneBySigningKey(String signingKey);
}