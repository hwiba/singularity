package singularity.user.domain;

public interface User {
	
	void changeName(String name);
	
	void changeProfileImage(String profileImage);
	
	void delete();
	
	void accept();
	
	boolean isAccept();
	
	Long getId();
	
	void setId(Long id);

	void ready();

	void changeEmail(String email);

    String getEmail();

    String getName();

    String getPassword();

    String getProfileImage();

}
