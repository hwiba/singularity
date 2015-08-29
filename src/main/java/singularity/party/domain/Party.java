package singularity.party.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import singularity.user.domain.User;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Party {

	public enum Openness {
		EVERYONE, COMMUNITY, SECRET, CLOSE
	}

	@Id
	@GeneratedValue
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	@Size(min = 1, max = 50)
	@Column(name = "name", length = 25, nullable = false)
	private String name;

	@Column(name = "image", length = 200, nullable = true)
	private String backgroundImage;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_FK")
	private User admin;

	@ManyToMany(fetch = FetchType.EAGER)
	private List<User> members;

	@Column(name = "openness", nullable = false)
	private Openness openness;

	public Party(final Date createDate, final String name, final String backgroundImage, final User admin, final Openness openness) {
		this.createDate = createDate;
		this.name = name;
		this.backgroundImage = backgroundImage;
		this.admin = admin;
		this.openness = openness;
		this.members = new ArrayList<>();
		this.addMember(admin);
	}

	public void changeName(final String name) {
		this.name = name;
	}

	public void changeBackgroundImage(final String backgroundImage) {
		this.backgroundImage = backgroundImage;
	}

	public void addMember(final User user) { this.members.add(user);}

	public void deleteMember(final User user) { this.members.remove(user); }

	public boolean hasMember(final User user) {
		return this.members.contains(user);
	}

	public boolean isAdmin(final User user) {
		return this.admin.equals(user);
	}

	public void changeAdmin(final User user) {
		this.admin = user;
	}

	public boolean isSecret() {
		return this.openness.equals(Openness.SECRET);
	}

	public boolean isCommunity() {
		return this.openness.equals(Openness.COMMUNITY);
	}

	public boolean isEveryone() {
		return this.openness.equals(Openness.EVERYONE);
	}

	public boolean isClose() {
		return this.openness.equals(Openness.CLOSE);
	}

	public void everyone() {
		this.openness = Openness.EVERYONE;
	}

	public void community() {
		this.openness = Openness.COMMUNITY;
	}

	public void secret() {
		this.openness = Openness.SECRET;
	}

	public void close() {
		this.openness = Openness.CLOSE;
	}

}
