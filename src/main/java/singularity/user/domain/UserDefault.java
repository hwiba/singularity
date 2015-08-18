package singularity.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDefault implements User {
	
	private enum MembershipClass {
		READY, ACCEPT, DELETE
	}

	@Id
	@GeneratedValue
	private Long id;

	@Email
	@Size(max = 50)
	private String email;

	@JsonIgnore
	@Pattern(regexp = "([a-zA-Z].*[0-9])|([0-9].*[a-zA-Z])")
	@Size(min = 8, max = 20)
	@Column(name = "password", length=20, nullable = false)
	private String password;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	@Pattern(regexp = "([a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣].*)")
	@Size(min = 2, max = 25)
	@Column(name = "name", length=25, nullable = false)
	private String name;

	@Column(name = "image", length=200, nullable = true)
	private String profileImage;

	@Column
	private MembershipClass membershipClass;

	public MembershipClass getMembershipClass() {
		return this.membershipClass;
	}
	
	@Override
	public void changeName(String name) {
		this.name = name;
	}

	@Override
	public void changeProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	@Override
	public void delete() {
		this.membershipClass = MembershipClass.DELETE;
	}

	@Override
	public void accept() {
		this.membershipClass = MembershipClass.ACCEPT;
	}
	
	@Override
	public void ready() {
		this.membershipClass = MembershipClass.READY;
	}

	@Override
	public void changeEmail(String email) {
		this.email = email;
	}

    @Override
	public boolean isAccept() {
		return this.membershipClass.equals(MembershipClass.ACCEPT);
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

}
