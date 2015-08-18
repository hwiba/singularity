package singularity.comment.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Lob;

@Data
@EqualsAndHashCode(callSuper=false)
public class PCommentDefault extends CommentDefault implements PComment{
	
	@Column
	private int paragraphIndex;

	@Column
	private int totalSameText;

	@Column
	private int sameTextIndex;

    @Lob
	@Column
	private String tartgetText;
	
}
