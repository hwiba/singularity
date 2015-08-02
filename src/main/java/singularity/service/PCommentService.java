package singularity.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import singularity.domain.Party;
import singularity.domain.Note;
import singularity.domain.PComment;
import singularity.domain.User;
import singularity.dto.out.PCommentCountByP;
import singularity.dto.out.SessionUser;
import singularity.exception.UnpermittedAccessGroupException;
import singularity.exception.UnpermittedAccessPCommentException;
import singularity.repository.PartyRepository;
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
	private NoteService noteService;
	@Resource
	private PartyRepository groupRepository;
	@Resource
	private PartyService groupService;
	@Resource
	private UserRepository userRepository;
	//@Resource
	//private AlarmRepository alarmRepository;

	public PComment create(PComment pComment) {
		Note note = noteRepository.findOne(pComment.getNote().getNoteId());
		User user = userRepository.findOne(pComment.getUser().getId());
		Party group = groupRepository.findOneByNote(note);
		if (!groupService.checkMember(group, user)) {
			throw new UnpermittedAccessGroupException("권한이 없습니다. 그룹 가입을 요청하세요.");
		}
		User noteWriter = note.getUser();
		pComment = pCommentRepository.save(pComment);
		noteRepository.save(note);
		
		if(!user.getId().equals(noteWriter.getId())){
			//alarmRepository.createNewNotes(new Alarm(createAlarmId(), "P", pComment.getUser(), noteWriter, pComment.getNote()));
		}
		return pComment;
	}

//	private String createAlarmId() {
//		String alarmId = RandomFactory.getRandomId(10);
//		if(alarmDao.isExistAlarmId(alarmId)) {
//			return createAlarmId();
//		}
//		return alarmId;
//	}

	public List<PComment> listByPAndNote(int pId, long noteId) {
		return pCommentRepository.findAllByPIdAndNote(pId, noteRepository.findOne(noteId));
	}
	
	public List<PComment> listByNoteId(long noteId) {
		return pCommentRepository.findAllByNote(noteRepository.findOne(noteId));
	}
	
	public List<PCommentCountByP> countAllByNoteByP(long noteId) {
		return pCommentRepository.countAllByNoteByP(noteId);
	}

	//TODO delete와의 공용 로직 메서드 분리할 것.
	public PComment update(String pCommentId, String pCommentText, SessionUser sessionUser) {
		PComment pComment = pCommentRepository.findOne(pCommentId);
		if (!sessionUser.getId().equals(pComment.getUser().getId())) {
			throw new UnpermittedAccessPCommentException("수정할 권한이 없는 코멘트입니다.");
		}
		pComment.setPCommentText(pCommentText);
		pComment = pCommentRepository.save(pComment);
		return pComment;
	}

	public void delete(String pCommentId, SessionUser sessionUser) {
		PComment pComment = pCommentRepository.findOne(pCommentId);
		if (!sessionUser.getId().equals(pComment.getUser().getId())) {
			throw new UnpermittedAccessPCommentException("수정할 권한이 없는 코멘트입니다.");
		}
		//noteRepository.decreaseCommentCountByPComment(pCommentId);
		pCommentRepository.delete(pCommentId);
	}

	public void updateParagraphId(List<Map<String, Object>> pCommentList) {
		for(Map<String, Object> pComment:pCommentList){
			//pCommentDao.updatePId(pComment.get("pCommentId").toString(), pComment.get("pId").toString());
		}
	}
}
