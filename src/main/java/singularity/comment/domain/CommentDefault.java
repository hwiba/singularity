package singularity.comment.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import singularity.user.domain.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDefault implements Comment {
	
	private enum Status {
		ON, OFF
	}

	@Id
	@GeneratedValue
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	@ManyToOne(fetch = FetchType.EAGER)
	private User writer;

	@Lob
	@Column(nullable=false)
	private String text;

	@Column(nullable = false)
	private Status status;

	@Override
	public boolean isWriter(User user) {
		return this.writer.equals(user);
	}

	@Override
	public void rewrite(String text) {
		if (null == text) {
			return;
		}
		this.text = text;
	}

	@Override
	public void delete() {
		this.status = Status.OFF;
	}
	
}
