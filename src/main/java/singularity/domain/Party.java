package singularity.domain;

import java.util.Date;
import java.util.List;

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

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Party {
	@Id
	@Size(max = 5)
	private String partyId;
	
	@Temporal(TemporalType.TIMESTAMP)
	private	Date createDate;
	
	@Size(min = 1, max = 50)
	@Column(name = "partyName", length=25, nullable = false)
	private String partyName;
	
	@ManyToMany(fetch=FetchType.EAGER)
	private List<User> users;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "user_FK")
	private User adminUser;
	
	@Column(name = "status", length=3, nullable = false)
	private String status;
	
	@Column(name = "image", length=200, nullable = true)
	private String partyImage;
	
	public Party(String partyId, String partyName, List<User> users, User adminUser, String status) {
		this(partyId, null, partyName, users, adminUser, status, "background-default.png");
	}

	public boolean checkCaptain(String userId) {
		return this.adminUser.getId().equals(userId);
	}

}
