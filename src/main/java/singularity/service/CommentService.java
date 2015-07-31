package singularity.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import singularity.domain.Comment;
import singularity.domain.Group;
import singularity.domain.Note;
import singularity.domain.User;
import singularity.dto.out.SessionUser;
import singularity.exception.UnpermittedAccessGroupException;
import singularity.repository.CommentRepository;
import singularity.repository.GroupRepository;
import singularity.repository.NoteRepository;
import singularity.repository.UserRepository;

@Service
@Transactional
public class CommentService {
	@Resource
	private CommentRepository commentRepository;
	@Resource
	private NoteRepository noteRepository;
//	@Resource
//	private AlarmDao alarmDao;
	@Resource
	private GroupRepository groupRepository;
	@Resource
	private GroupService groupService;
	@Resource
	private UserRepository userRepository;

	public List<Comment> create(SessionUser sessionUser, Note note, Comment comment) {
		note = noteRepository.findOne(note.getNoteId());
		Group group = note.getGroup();
		User user = userRepository.findOne(sessionUser.getId());
		if (!groupService.checkGroupMember(group, user)) {
			throw new UnpermittedAccessGroupException("권한이 없습니다. 그룹 가입을 요청하세요.");
		}
		comment.setCreateDate(new Date());
		comment.setNote(note);
		comment.setUser(user);
		comment = commentRepository.save(comment);
		note.setCommentCount(note.getCommentCount() + 1);
		noteRepository.save(note);
		//createAlarm(comment);
		return commentRepository.findAllByNote(comment.getNote());
	}

//	private void createAlarm(Comment comment) {
//		Note note = comment.getNote();
//		User noteWriter = noteDao.readNote(note.getNoteId()).getUser();
//		if (!comment.checkWriter(noteWriter)) {
//			alarmDao.createNewComments(new Alarm(createAlarmId(), "C", comment.getUser(), noteWriter, note, comment));
//		}
//	}

//	private String createAlarmId() {
//		String alarmId = RandomFactory.getRandomId(10);
//		if(alarmDao.isExistAlarmId(alarmId)) {
//			return createAlarmId();
//		}
//		return alarmId;
//	}

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
