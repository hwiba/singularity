package singularity.note.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import singularity.comment.repository.CommentRepository;
import singularity.note.domain.Note;
import singularity.note.repository.NoteRepository;
import singularity.notification.domain.Notification;
import singularity.notification.repository.NotificationRepository;
import singularity.party.domain.Party;
import singularity.party.repository.PartyRepository;
import singularity.user.domain.User;
import singularity.user.repository.UserRepository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class NoteService {
	@Resource
	private PartyRepository partyRepository;
	@Resource
	private NoteRepository noteRepository;
	@Resource
	private UserRepository userRepository;
	@Resource
	private CommentRepository commentRepository;
    @Resource
    private NotificationRepository notificationRepository;

	public Note findOne(long noteId) {
		return noteRepository.findOne(noteId);
	}

    public void delete(long noteId) {
        Note note = noteRepository.findOne(noteId);
        note.delete();
    }

    public List<Note> readByGroupPage(Party party) {
        return noteRepository.findAllByParty(party);
    }

	public void create(Long sessionId, Long partyId, String noteText) {
		Party party = partyRepository.findOne(partyId);
		User user = userRepository.findOne(sessionId);
		if (!party.hasMember(user)) {
			throw new IllegalArgumentException("권한이 없습니다. 그룹 가입을 요청하세요.");
		}
		Note note = new Note();
		note.setWriter(user);
		note.setParty(party);
		note.setCreateDate(new Date());
		note.setText(noteText);
		noteRepository.save(note);
        // TODO MQ 적용이 필요
		this.sendNotification(user, party);
	}

    private void sendNotification(User writer, Party party) {
        List<User> members = party.getMembers();
        for (User member : members) {
            notificationRepository.save(new Notification(new Date(), writer, member, party, Notification.Pattern.NEW_POST));
        };

    }

	public void update(Long noteId, String noteText) throws Throwable {
//		Note note = noteRepository.findOne(noteId);
//		//TODO pComment 위치 변경하기. <- pcomment domain으로 로직 이동
//		try {
//			String newMarkdown = (String) new NashornEngine().markdownToHtml(noteText);
//			String oldMarkdown = (String) new NashornEngine().markdownToHtml(note.getNoteText());
//
//			Document newDoc = Jsoup.parse(newMarkdown);
//			Document oldDoc = Jsoup.parse(oldMarkdown);
//
//			Elements newPTags = newDoc.getElementsByClass("pCommentText");
//			Elements oldPTags = oldDoc.getElementsByClass("pCommentText");
//
//			String[] newTextParagraph = new String[newPTags.size()];
//			String[] oldTextParagraph = new String[oldPTags.size()];
//
//			int i = 0, k = 0;
//			for (Element pTag : newPTags) {
//				newTextParagraph[i++] = pTag.text();
//			}
//			for (Element pTag : oldPTags) {
//				oldTextParagraph[k++] = pTag.text();
//			}
//
//			note.setPComments(PCommentBinding.UpdatePId(oldTextParagraph, newTextParagraph, note.getPComments()));
//
//			note.setNoteText(noteText);
//			note.setNoteTargetDate(new Date());
//		} catch (Throwable e) {
//			throw new Exception("노트 저장에 실패했습니다.");
//		}
	}

}
