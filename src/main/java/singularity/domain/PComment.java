package singularity.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class PComment {
	
	@Id
	@GeneratedValue
	private long pCommentId;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	
	@Column
	private int pId;
	
	@Column
	private int sameSenCount;
	
	@Column
	private int sameSenIndex;
	
	@Lob
	@Column(nullable=false)
	private String pCommentText;
	
	@Lob
	@Column(nullable=false)
	private String selectedText;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name="user_FK")
	private User user;
	
	public boolean isOwner(User user) {
		return this.user.equals(user);
	}
}
