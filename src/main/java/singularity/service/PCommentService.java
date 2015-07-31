package singularity.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import singularity.domain.Group;
import singularity.domain.Note;
import singularity.domain.PComment;
import singularity.domain.User;
import singularity.dto.out.SessionUser;
import singularity.exception.UnpermittedAccessGroupException;
import singularity.exception.UnpermittedAccessPCommentException;
import singularity.repository.GroupRepository;
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
	private GroupRepository groupRepository;
	@Resource
	private GroupService groupService;
	@Resource
	private UserRepository userRepository;
	//@Resource
	//private AlarmRepository alarmRepository;

	public PComment create(PComment pComment) {
		Note note = noteRepository.findOne(pComment.getNote().getNoteId());
		User user = userRepository.findOne(pComment.getUser().getId());
		Group group = groupRepository.findOneByNote(note);
		if (!groupService.checkGroupMember(group, user)) {
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

	public List<PComment> list(String pId, String noteId) {
		//TODO note가 pComment list를 가지고 있다면 모양이 전혀 달라질 듯.
		// TODO 만약 note가 Text를 가지지 않고, p의 리스트를 가지고 있다면.?
		return pCommentRepository.findAllByPIdAndNote(pId, noteId);
	}
	
	public List<PComment> listByNoteId(long noteId) {
		return pCommentRepository.findAllByNote(noteRepository.findOne(noteId));
	}
	
	public List<Map<String, Object>> countByPGroupPCommnent(String noteId) {
		return pCommentDao.countByPGroupPCommnent(noteId);
	}

	public Object update(String pCommentId, String pCommentText, SessionUser sessionUser) {
		PComment pComment = pCommentDao.readByPCommentId(pCommentId);
		if (!sessionUser.getUserId().equals(pComment.getSessionUser().getUserId())) {
			throw new UnpermittedAccessPCommentException("수정할 권한이 없는 코멘트입니다.");
		}
		pCommentDao.updatePComment(pCommentId, pCommentText);
		return pCommentDao.readByPCommentId(pCommentId);
	}

	public void delete(String pCommentId, SessionUser sessionUser) {
		PComment pComment = pCommentDao.readByPCommentId(pCommentId);
		if (!sessionUser.getUserId().equals(pComment.getSessionUser().getUserId())) {
			throw new UnpermittedAccessPCommentException("삭제할 권한이 없는 코멘트입니다.");
		}
		noteDao.decreaseCommentCountByPComment(pCommentId);
		pCommentRepository.delete(pCommentId);
	}

	public void updateParagraphId(List<Map<String, Object>> pCommentList) {
		for(Map<String, Object> pComment:pCommentList){
			pCommentDao.updatePId(pComment.get("pCommentId").toString(), pComment.get("pId").toString());
		}
	}
}
