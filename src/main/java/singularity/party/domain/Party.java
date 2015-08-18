package singularity.party.domain;


import singularity.user.domain.User;

public interface Party {

	void changeName(String name);
	
	void changeBackgroundImage(String backgroundImage);

	void addMember(User user);

	void deleteMember(User user);

	void hasMember(User user);

	boolean isAdmin(User user);

	void changeAdmin(User user);

	void classSetPublic();
	
	void classSetParty();
	
	void classSetPrivate();
	
	void classSetClose();

	boolean isPrivateClass();
	
	boolean isPartyClass();

	boolean isPublicClass();
	
	boolean isCloseClass();

}
