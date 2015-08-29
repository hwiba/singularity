package singularity.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import singularity.notification.domain.Notification;
import singularity.party.domain.Party;
import singularity.user.domain.User;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long>{
	
	List<Notification> findOneByPartyAndWriterAndReader(Party party, User writer, User reader);

}
