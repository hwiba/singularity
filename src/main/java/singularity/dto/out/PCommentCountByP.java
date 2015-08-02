package singularity.dto.out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PCommentCountByP {
	private long pId;
	private long count;
}
