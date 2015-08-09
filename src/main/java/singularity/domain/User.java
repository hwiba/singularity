package singularity.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import singularity.enums.UserStatus;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
	@Id
	@Email
	@Size(max = 50)
    private String id;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	@Pattern(regexp = "([a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣].*)")
	@Size(min = 2, max = 25)
	@Column(name = "name", length=25, nullable = false)
	private String name;
	
	@JsonIgnore
	@Pattern(regexp = "([a-zA-Z].*[0-9])|([0-9].*[a-zA-Z])")
	@Size(min = 8, max = 20)
	@Column(name = "password", length=20, nullable = false)
	private String password;

	@JsonIgnore
	@Column(name = "status", nullable = false)
	private UserStatus status;
	
	@Column(name = "image", length=200, nullable = true)
	private String image;
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private List<Notification> notification;

	public User(String id, String name, String password) {
		this(id, null, name, password, UserStatus.READY, null, null);
	}
	
	public User(String userId) {
		this(userId, null, null);
	}
	
}
