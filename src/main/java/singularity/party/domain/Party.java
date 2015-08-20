package singularity.party.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import singularity.user.domain.User;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Party {
	
	private enum Openness {
		PUBLIC, PARTY, PRIVATE, CLOSE
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

	public void hasMember(User user) {
		this.members.contains(user);
	}

	public boolean isAdmin(User user) {
		return this.admin.equals(user);
	}

	public void changeAdmin(User user) {
		this.admin = user;
	}

	public boolean isPrivateClass() {
		return this.openness.equals(Openness.PRIVATE);
	}

	public boolean isPartyClass() {
		return this.openness.equals(Openness.PARTY);
	}

	public boolean isPublicClass() {
		return this.openness.equals(Openness.PUBLIC);
	}

	public boolean isCloseClass() {
		return this.openness.equals(Openness.CLOSE);
	}

	public void classSetPublic() {
		this.openness = Openness.PUBLIC;
	}

	public void classSetParty() {
		this.openness = Openness.PARTY;
	}

	public void classSetPrivate() {
		this.openness = Openness.PRIVATE;
	}

	public void classSetClose() {
		this.openness = Openness.CLOSE;
	}

}
