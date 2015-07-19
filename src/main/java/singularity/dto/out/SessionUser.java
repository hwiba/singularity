package singularity.dto.out;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import singularity.domain.User;
import singularity.enums.UserStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionUser {
	private String id;
	private String name;
	private UserStatus status;
	private String image;
	
	public SessionUser(User user) {
		this.id = user.getId();
		this.name = user.getName();
		this.status = user.getStatus();
		this.image = user.getImage();
	}
	
}
