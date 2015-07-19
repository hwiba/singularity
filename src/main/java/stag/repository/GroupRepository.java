package stag.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import stag.domain.Crowd;
import stag.domain.User;

public interface GroupRepository extends JpaRepository<Crowd, String>{
	
	List<Crowd> findAllByUsers(User user);

	Crowd findOneByGroupIdAndUsers(User user, String groupId);
	
}