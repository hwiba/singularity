package singularity.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@AllArgsConstructor
@NoArgsConstructor
public class Note {
	@Id
	@GeneratedValue
	private long noteId;
	
	@Lob
	@Column(nullable = false, columnDefinition="text")
	private String noteText;
	
	@Temporal(TemporalType.DATE)
	private Date noteTargetDate;
	
	@ManyToOne
	@JoinColumn(name = "user_FK")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "party_FK")
	private Party party;
	
	@Column
	private int commentCount;
}
