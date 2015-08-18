package singularity.party.domain;

import lombok.Data;
import singularity.user.domain.User;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class PartyDefault implements Party {
	
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

	@Override
	public void changeName(String name) {
		this.name = name;
	}
	
	@Override
	public void changeBackgroundImage(String backgroundImage) {
		this.backgroundImage = backgroundImage;
	}

	@Override
	public void addMember(User user) {
		this.members.add(user);
		
	}
	
	@Override
	public void deleteMember(User user) {
		this.members.remove(user);
		
	}

	@Override
	public void hasMember(User user) {
		this.members.contains(user);
	}

	@Override
	public boolean isAdmin(User user) {
		return this.admin.equals(user);
	}

	@Override
	public void changeAdmin(User user) {
		this.admin = user;
	}

	@Override
	public boolean isPrivateClass() {
		return this.openness.equals(Openness.PRIVATE);
	}

	@Override
	public boolean isPartyClass() {
		return this.openness.equals(Openness.PARTY);
	}

	@Override
	public boolean isPublicClass() {
		return this.openness.equals(Openness.PUBLIC);
	}

	@Override
	public boolean isCloseClass() {
		return this.openness.equals(Openness.CLOSE);
	}

	@Override
	public void classSetPublic() {
		this.openness = Openness.PUBLIC;
	}

	@Override
	public void classSetParty() {
		this.openness = Openness.PARTY;
	}

	@Override
	public void classSetPrivate() {
		this.openness = Openness.PRIVATE;
	}

	@Override
	public void classSetClose() {
		this.openness = Openness.CLOSE;
	}

}
