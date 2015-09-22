package singularity.user.domain;

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

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Confirm {

	private enum Status {
		READY, CHECKED
	}

	@Id
	@GeneratedValue
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_FK")
	private User user;

	@Column
	private Status stauts;

	@Column(nullable = false)
	private String identificationKey;


    public Confirm(Date createData, User user, String identificationKey) {
        this.stauts = Status.READY;
        this.createDate = createData;
        this.user = user;
        this.identificationKey = identificationKey;

    }

	public void delete() {
		this.stauts = Status.CHECKED;
	}

	public boolean isIdentificationKey(String keyword) {
		return this.identificationKey.equals(keyword);
	}

    public String getUserEmail() { return this.user.getEmail(); }

    public String getUserName() { return this.user.getName(); }
	
}
