package singularity.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import singularity.enums.NotificationStatus;;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

	@Id
	@GeneratedValue
	private long Id;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "user_FK")
	private User caller;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "notification_FK")
	private Party party;
	
	@Column
	private NotificationStatus status;
	
	public Notification(User caller, Party party, NotificationStatus status) {
		this(0, new Date(), caller, party, status);
	}
	
}
