package singularity.notification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import singularity.notification.domain.Notification;
import singularity.party.domain.Party;
import singularity.user.domain.User;

public interface NotificationRepository extends JpaRepository<Notification, Long>{
	
	List<Notification> findOneByPartyAndWriterAndReader(Party party, User writer, User reader);

}
