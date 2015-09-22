package singularity.comment.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import singularity.comment.domain.Comment;
import singularity.comment.repository.CommentRepository;
import singularity.note.domain.Note;
import singularity.note.repository.NoteRepository;
import singularity.party.domain.Party;
import singularity.user.domain.User;
import singularity.user.repository.UserRepository;


@Service
@Transactional
public class CommentService {
	@Resource
	private CommentRepository commentRepository;
	@Resource
	private NoteRepository noteRepository;
	@Resource
	private UserRepository userRepository;

    public CommentService() {
    }

    public Comment create(Comment comment, long noteId, final Long sessionId) throws IllegalAccessException {
		User user = userRepository.findOne(sessionId);
		Note note = noteRepository.findOne(noteId);
		Party party = note.getParty();
		if (!party.hasMember(user)) {
			throw new IllegalAccessException("권한이 없습니다. 그룹 가입을 요청하세요.");
		}
        commentRepository.save(comment);
		return comment;
	}


	public List<Comment> findAllByPId(final Integer pageId, final long noteId) {
		Note note = noteRepository.findOne(noteId);
		return commentRepository.findAllByNoteAndPageId(note, pageId);
	}

	public List<Comment> findAllByNoteId(long noteId) {
		return commentRepository.findAllByNote(noteRepository.findOne(noteId));
	}

	public Comment update(final Long pCommentId, final String pCommentText, final Long sessionId) throws IllegalAccessException {
		Comment comment = commentRepository.findOne(pCommentId);
		this.accessPermit(sessionId, comment);
		comment.setText(pCommentText);
		return comment;
	}

	public void delete(final Long pCommentId, final Long sessionId) throws IllegalAccessException {
		Comment comment = commentRepository.findOne(pCommentId);
		this.accessPermit(sessionId, comment);
		commentRepository.delete(pCommentId);
	}

	private void accessPermit(final Long sessionId, final Comment comment) throws IllegalAccessException {
		User user = userRepository.findOne(sessionId);
		if (!comment.isWriter(user)) {
			throw new IllegalAccessException("수정할 권한이 없는 코멘트입니다.");
		}
	}
}
