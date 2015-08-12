package singularity.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import singularity.enums.NotificationStatus;
import singularity.enums.Openness;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Party {
	@Id
	@Size(max = 5)
	private String partyId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	@Size(min = 1, max = 50)
	@Column(name = "partyName", length = 25, nullable = false)
	private String partyName;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<User> users;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_FK")
	private User adminUser;

	@Column(name = "openness", length = 3, nullable = false)
	private Openness openness;

	@Column(name = "image", length = 200, nullable = true)
	private String partyImage;

	public Party(String partyId, String partyName, List<User> users, User adminUser, Openness openness) {
		this(partyId, null, partyName, users, adminUser, openness, "background-default.png");
	}

	public boolean isAdmin(User user) {
		return this.adminUser.equals(user);
	}

	public boolean hasUser(User user) {
		return this.users.contains(user);
	}

	public boolean addUser(User user) {
		return this.users.add(user);
	}

	public boolean deleteUser(User user) {
		return this.users.remove(user);
	}

	public boolean SingUpRequest(User user) {
		return this.adminUser.getNotification().add(new Notification(user, this, NotificationStatus.REQUEST));
	}

	public boolean isAlreadyRequest(User user) {
		List<Notification> notifications = this.adminUser.getNotification();
		for (Notification notification : notifications) {
			if (notification.getCaller().equals(user) && notification.getParty().equals(this)
					&& notification.getStatus().equals(NotificationStatus.REQUEST)) {
				return true;
			}
		}
		return false;
	}
	
	public void sendNotification(Notification notification) {
		for (User user : users) {
			if(!user.equals(notification.getCaller())) {
				user.getNotification().add(notification);
			}
		}
	}
	
	
}
