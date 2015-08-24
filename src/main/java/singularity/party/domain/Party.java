package singularity.party.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import singularity.user.domain.User;

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
	private User admin;

	@ManyToMany(fetch = FetchType.EAGER)
	private List<User> members;

	@Column(name = "openness", nullable = false)
	private Openness openness;

	public Party(Date createDate, String name, String backgroundImage, User admin, Openness openness) {
		this.createDate = createDate;
		this.name = name;
		this.backgroundImage = backgroundImage;
		this.admin = admin;
		this.openness = openness;
		this.members = new ArrayList();
		this.addMember(admin);
	}

	public void changeName(String name) {
		this.name = name;
	}

	public void changeBackgroundImage(String backgroundImage) {
		this.backgroundImage = backgroundImage;
	}

	public void addMember(User user) {
		this.members.add(user);

	}

	public void deleteMember(User user) {
		this.members.remove(user);

	}

	public boolean hasMember(User user) {
		return this.members.contains(user);
	}

	public boolean isAdmin(User user) {
		return this.admin.equals(user);
	}

	public void changeAdmin(User user) {
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
