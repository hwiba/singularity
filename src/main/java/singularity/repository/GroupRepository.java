package singularity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import singularity.domain.Group;
import singularity.domain.User;

public interface GroupRepository extends JpaRepository<Group, String>{
	
	List<Group> findAllByUsers(User user);

	Group findOneByGroupIdAndUsers(User user, String groupId);
	
}