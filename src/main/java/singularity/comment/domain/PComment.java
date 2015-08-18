package singularity.comment.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
public class PComment extends Comment{
	
	@Column
	private int paragraphIndex;

	@Column
	private int totalSameText;

	@Column
	private int sameTextIndex;

    @Lob
	@Column
	private String targetText;
	
}
