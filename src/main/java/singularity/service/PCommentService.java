package singularity.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import singularity.domain.Note;
import singularity.domain.PComment;
import singularity.domain.Party;
import singularity.domain.User;
import singularity.dto.out.SessionUser;
import singularity.exception.UnpermittedAccessGroupException;
import singularity.exception.UnpermittedAccessPCommentException;
import singularity.repository.NoteRepository;
import singularity.repository.PCommentRepository;
import singularity.repository.UserRepository;


@Service
@Transactional
public class PCommentService {
	@Resource
	private PCommentRepository pCommentRepository;
	@Resource
	private NoteRepository noteRepository;
	@Resource
	private UserRepository userRepository;

	public PComment create(PComment pComment, long noteId, SessionUser sessionUser) {
		User user = userRepository.findOne(sessionUser.getId());
		Note note = noteRepository.findOne(noteId);
		Party party = note.getParty();
		if (!party.hasUser(user)) {
			throw new UnpermittedAccessGroupException("권한이 없습니다. 그룹 가입을 요청하세요.");
		}
		note.add(pComment);
		return pComment;
	}


	public List<PComment> findAllByPId(int pId, long noteId) {
		Note note = noteRepository.findOne(noteId);
		return note.findPCommentsByPId(pId);
	}
	
	public List<PComment> findAllByNoteId(long noteId) {
		return noteRepository.findOne(noteId).getPComments();
	}
	
	public PComment update(String pCommentId, String pCommentText, SessionUser sessionUser) {
		PComment pComment = pCommentRepository.findOne(pCommentId);
		this.accessPermit(sessionUser, pComment);
		pComment.setPCommentText(pCommentText);
		return pComment;
	}

	public void delete(String pCommentId, SessionUser sessionUser) {
		PComment pComment = pCommentRepository.findOne(pCommentId);
		this.accessPermit(sessionUser, pComment);
		pCommentRepository.delete(pCommentId);
	}
	
	private void accessPermit(SessionUser sessionUser, PComment pComment) {
		User user = userRepository.findOne(sessionUser.getId());
		if (!pComment.isOwner(user)) {
			throw new UnpermittedAccessPCommentException("수정할 권한이 없는 코멘트입니다.");
		}
	}
	
//	XXX pc 카운트 쿼리 만들기
//	public List<PCommentCountByP> countAllByNoteByP(long noteId) {
//		return pCommentRepository.countAllByNoteByP(noteId);
//	}

}
