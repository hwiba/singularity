package singularity.domain;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
	@Column(nullable = false, columnDefinition = "text")
	private String noteText;

	@Temporal(TemporalType.TIMESTAMP)
	private Date noteTargetDate;

	@ManyToOne
	@JoinColumn(name = "user_FK")
	private User user;

	@ManyToOne
	@JoinColumn(name = "party_FK")
	private Party party;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Comment> comments;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<PComment> pComments;

	public void add(PComment pComment) {
		this.pComments.add(pComment);
	}

	public List<PComment> findPCommentsByPId(int pId) {
		return this.pComments.stream().filter(pComment -> pComment.getPId() == pId).collect(Collectors.toList());
	}

}
