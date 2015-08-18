package singularity.comment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommentService {
//	@Resource
//	private CommentRepository commentRepository;
//	@Resource
//	private NoteRepository noteRepository;
//	@Resource
//	private UserRepository userRepository;
//
//	public List<Comment> create(SessionUser sessionUser, Note note, Comment comment) {
//		note = noteRepository.findOne(note.getNoteId());
//		Party party = note.getParty();
//		User user = userRepository.findOne(sessionUser.getId());
//		if (!party.hasUser(user)) {
//			throw new UnpermittedAccessGroupException("권한이 없습니다. 그룹 가입을 요청하세요.");
//		}
//		comment.setCreateDate(new Date());
//		comment.setUser(user);
//		comment = commentRepository.save(comment);
//		noteRepository.save(note);
//		return note.getComments();
//	}
//
//	public List<Comment> findAll(long noteId) {
//		return noteRepository.findOne(noteId).getComments();
//	}
//
//	public Comment update(long commentId, String commentText) {
//		Comment comment = commentRepository.findOne(commentId);
//		comment.setCommentText(commentText);
//		commentRepository.save(comment);
//		return comment;
//	}
//
//	public void delete(long commentId) {
//		commentRepository.delete(commentId);
//	}
}
