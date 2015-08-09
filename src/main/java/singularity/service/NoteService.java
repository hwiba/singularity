package singularity.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import singularity.domain.Note;
import singularity.domain.Notification;
import singularity.domain.Party;
import singularity.domain.User;
import singularity.enums.NotificationStatus;
import singularity.exception.UnpermittedAccessGroupException;
import singularity.repository.NoteRepository;
import singularity.repository.PartyRepository;
import singularity.repository.UserRepository;

@Service
@Transactional
public class NoteService {
	@Resource
	private PartyRepository partyRepository;
	@Resource
	private PartyService partyService;
	@Resource
	private NoteRepository noteRepository;
	@Resource
	private UserRepository userRepository;
	@Resource
	private PCommentService pCommentService;

	public Note read(long noteId) {
		return noteRepository.findOne(noteId);
	}

	public void create(String sessionUserId, String partyId, String noteText, Date noteTargetDate, String tempNoteId) {
		Party party = partyRepository.findOne(partyId);
		User user = userRepository.findOne(sessionUserId);
		if (!party.hasUser(user)) {
			throw new UnpermittedAccessGroupException("권한이 없습니다. 그룹 가입을 요청하세요.");
		}
		Note note = new Note();
		note.setUser(user);
		note.setParty(party);
		note.setCommentCount(0);
		note.setNoteTargetDate(noteTargetDate);
		note.setNoteText(noteText);
		noteRepository.save(note);
		party.sendNotification(new Notification(user, party, NotificationStatus.NEW_POST));
	}

	public void update(String noteText, long noteId, Date noteTargetDate, List<Map<String, Object>> pCommentList) {
		Note note = noteRepository.findOne(noteId);
		note.setNoteText(noteText);
		note.setNoteTargetDate(noteTargetDate);
		noteRepository.save(note);
		//pCommentService.updateParagraphId(pCommentList);
	}

	public void delete(long noteId) {
		noteRepository.delete(noteId);
	}
	
	public List<Note> readByGroupPage(Party group) {
		return noteRepository.findAllByParty(group);
	}

//	public List<Boolean> readNullDay(String groupId, String lastDate) throws ParseException {
//		DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
//		DateTime date = dtf.parseDateTime(lastDate);
//		date = date.withTime(0, 0, 0, 0);
//		date = date.withDayOfMonth(1);
//		String startDate = dtf.print(date);
//		List<String> list = noteDao.readNotesByDate(groupId, startDate, lastDate);
//		Collections.sort(list);
//		List<Boolean> nullDay = new ArrayList<Boolean>();
//		for(int i=0;i < dtf.parseDateTime(lastDate).getDayOfMonth(); i++){
//			nullDay.add(true);
//		}
//		for (String string : list) {
//			nullDay.set(dtf.parseDateTime(string).getDayOfMonth()-1, false);
//		}
//		return nullDay;
//	}
}
