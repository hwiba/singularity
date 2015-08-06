package singularity.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import singularity.domain.Comment;
import singularity.domain.Note;
import singularity.domain.Party;
import singularity.domain.User;
import singularity.dto.out.SessionUser;
import singularity.exception.UnpermittedAccessGroupException;
import singularity.repository.CommentRepository;
import singularity.repository.NoteRepository;
import singularity.repository.UserRepository;

@Service
@Transactional
public class CommentService {
	@Resource
	private CommentRepository commentRepository;
	@Resource
	private NoteRepository noteRepository;
	@Resource
	private UserRepository userRepository;

	public List<Comment> create(SessionUser sessionUser, Note note, Comment comment) {
		note = noteRepository.findOne(note.getNoteId());
		Party party = note.getParty();
		User user = userRepository.findOne(sessionUser.getId());
		if (!party.hasUser(user)) {
			throw new UnpermittedAccessGroupException("권한이 없습니다. 그룹 가입을 요청하세요.");
		}
		comment.setCreateDate(new Date());
		comment.setNote(note);
		comment.setUser(user);
		comment = commentRepository.save(comment);
		note.setCommentCount(note.getCommentCount() + 1);
		noteRepository.save(note);
		return commentRepository.findAllByNote(comment.getNote());
	}

	public List<Comment> list(long noteId) {
		Note note = noteRepository.findOne(noteId);
		return commentRepository.findAllByNote(note);
	}

	public Comment update(long commentId, String commentText) {
		Comment comment = commentRepository.findOne(commentId);
		comment.setCommentText(commentText);
		commentRepository.save(comment);
		return comment;
	}

	public void delete(long commentId) {
		Note note = noteRepository.findOne(commentRepository.findOne(commentId).getNote().getNoteId());
		note.setCommentCount(note.getCommentCount() -1);
		commentRepository.delete(commentId);
	}
}
