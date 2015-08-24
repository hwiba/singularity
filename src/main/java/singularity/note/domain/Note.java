package singularity.note.domain;

import lombok.Data;
import singularity.party.domain.Party;
import singularity.user.domain.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Note {
	
	private enum Openness {
		DRAFT, PUBLISH, DELETE
	}

    @Id
    @GeneratedValue
	private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Lob
    @Column(nullable = false, columnDefinition = "text")
    private String text;

    @ManyToOne
    @JoinColumn(name = "user_FK")
    private User writer;

    @ManyToOne
    @JoinColumn(name = "party_FK")
    private Party party;

    @Column
    private Openness openness;
	
	public boolean isWriter(User user) {
		return this.writer.equals(user);
	}

	public boolean isDraft() {
		return this.openness.equals(Openness.DRAFT);
	}

	public boolean isPublish() {
		return this.openness.equals(Openness.PUBLISH);
	}
	
	public boolean isDelete() {
		return this.openness.equals(Openness.DELETE);
	}

	public void publish() {
		this.openness = Openness.PUBLISH;
	}

	public void draft() {
		this.openness = Openness.PUBLISH;
	}
	
	public void delete() {
		this.openness = Openness.DELETE;
	}

	public void rewrite(String text) {
		// TODO markdown 변환, pcomment 매칭 등 작업한 뒤.
		this.text = text;
	}
	
}