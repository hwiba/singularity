package singularity.comment.domain;


import singularity.user.domain.User;

public interface Comment {
	
	boolean isWriter(User user);
	
	void rewrite(String text);
	
	void delete();
}
