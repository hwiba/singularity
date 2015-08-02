package singularity.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import singularity.domain.Party;
import singularity.domain.Note;
import singularity.dto.out.SessionUser;
import singularity.exception.UnpermittedAccessGroupException;
import singularity.repository.PartyRepository;
import singularity.repository.NoteRepository;
import singularity.repository.UserRepository;

@Service
@Transactional
public class NoteService {
	@Resource
	private PartyRepository groupRepository;
	@Resource
	private PartyService groupService;
	@Resource
	private NoteRepository noteRepository;
	@Resource
	private UserRepository userRepository;
//	@Resource
//	private AlarmDao alarmDao;
//	@Resource
//	private TempNoteService tempNoteService;
//	@Resource
//	private PCommentService pCommentService;

	public Note read(long noteId) {
		return noteRepository.findOne(noteId);
	}

	public void create(String sessionUserId, String groupId, String noteText, Date noteTargetDate, String tempNoteId) {
		if (!groupService.checkMember(groupRepository.findOne(groupId), userRepository.findOne(sessionUserId))) {
			throw new UnpermittedAccessGroupException("권한이 없습니다. 그룹 가입을 요청하세요.");
		}
		Note note = new Note();
		note.setUser(userRepository.findOne(sessionUserId));
		note.setParty(groupRepository.findOne(groupId));
		note.setCommentCount(0);
		note.setNoteTargetDate(noteTargetDate);
		note.setNoteText(noteText);
		note = noteRepository.save(note);
		SessionUser sessionUser = new SessionUser(note.getUser());
		//String alarmId = null;
		//Alarm alarm = null;
//		List<User> groupMembers = groupRepository.findOne(groupId).getUsers();
//		for (User reader : groupMembers) {
//			if (reader.getUserId().equals(sessionUserId)) {
//				continue;
//			}
//			while (true) {
//				alarmId = RandomFactory.getRandomId(10);
//				if (!alarmDao.isExistAlarmId(alarmId)) {
//					alarm = new Alarm(alarmId, "N", sessionUser, reader, new Note(noteId));
//					break;
//				}
//			}
//			alarmDao.createNewNotes(alarm);
//		}
		//tempNoteService.delete(Long.parseLong(tempNoteId));
	}

	public void update(String noteText, long noteId, Date noteTargetDate, List<Map<String, Object>> pCommentList) {
		Note note = noteRepository.findOne(noteId);
		note.setNoteText(noteText);
		note.setNoteTargetDate(noteTargetDate);
		noteRepository.save(note);
		//pCommentService.updateParagraphId(pCommentList);
	}

	public void delete(long noteId) {
		//alarmRepository.deleteByNote(noteRepository.findOne(noteId));
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
