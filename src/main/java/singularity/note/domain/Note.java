package singularity.note.domain;


import singularity.user.domain.User;

public interface Note {
	
	boolean isWriter(User user);
	
	boolean isDraft();
	
	boolean isPublish();
	
	boolean isDelete();
	
	void publish();
	
	void draft();
	
	void delete();
	
	void rewrite(String text);
	
}
