package singularity.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import singularity.user.domain.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionUser {
	private Long id;
    private String email;
	private String name;
	private String profileImage;
	
	public SessionUser(User user) {
		this.id = user.getId();
		this.name = user.getName();
        this.email = user.getEmail();
		this.profileImage = user.getProfileImage();
	}

}
