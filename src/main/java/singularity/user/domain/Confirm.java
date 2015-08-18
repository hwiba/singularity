package singularity.user.domain;

public interface Confirm {

	void delete();
	
	boolean isIdentificationKey(String keyword);

    User getUser();

    String getIdentificationKey();

    String getUserEmail();

    String getUserName();

}
