package singularity.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
	
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

    public User(String email, String password, Date createDate, String name, String profileImage) {
        this.email = email;
        this.password = password;
        this.createDate = createDate;
        this.name = name;
        this.profileImage = profileImage;
        this.membershipClass = MembershipClass.READY;
    }

	public MembershipClass getMembershipClass() {
		return this.membershipClass;
	}
	
	public void changeName(String name) {
		this.name = name;
	}

	public void changeProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public void delete() {
		this.membershipClass = MembershipClass.DELETE;
	}

	public void accept() {
		this.membershipClass = MembershipClass.ACCEPT;
	}
	
	public void ready() {
		this.membershipClass = MembershipClass.READY;
	}

	public void changeEmail(String email) {
		this.email = email;
	}

	public boolean isAccept() {
		return this.membershipClass.equals(MembershipClass.ACCEPT);
	}

    public boolean isDelete() {
        return this.membershipClass.equals(MembershipClass.DELETE);
    }

    public boolean isReady() {
        return this.membershipClass.equals(MembershipClass.READY);
    }

	public void setId(Long id) {
		this.id = id;
	}

}
