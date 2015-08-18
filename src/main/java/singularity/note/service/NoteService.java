package singularity.note.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import singularity.domain.Note;
import singularity.enums.NotificationStatus;
import singularity.exception.UnpermittedAccessGroupException;
import singularity.note.repository.NoteRepository;
import singularity.comment.repository.PCommentRepository;
import singularity.party.repository.PartyRepository;
import singularity.user.repository.UserRepository;
import singularity.common.utility.NashornEngine;
import singularity.common.utility.PCommentBinding;

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
	private PCommentRepository pCommentRepository;

	public Note findOne(long noteId) {
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
		note.setNoteTargetDate(noteTargetDate);
		note.setNoteText(noteText);
		noteRepository.save(note);
		party.sendNotification(new Notification(user, party, NotificationStatus.NEW_POST));
	}

	public void update(long noteId, String noteText, Date noteTargetDate) throws Throwable {
		Note note = noteRepository.findOne(noteId);
		//XXX pComment 위치 변경하기.
		try {
			String newMarkdown = (String) new NashornEngine().markdownToHtml(noteText);
			String oldMarkdown = (String) new NashornEngine().markdownToHtml(note.getNoteText());

			Document newDoc = Jsoup.parse(newMarkdown);
			Document oldDoc = Jsoup.parse(oldMarkdown);

			Elements newPTags = newDoc.getElementsByClass("pCommentText");
			Elements oldPTags = oldDoc.getElementsByClass("pCommentText");

			String[] newTextParagraph = new String[newPTags.size()];
			String[] oldTextParagraph = new String[oldPTags.size()];

			int i = 0, k = 0;
			for (Element pTag : newPTags) {
				newTextParagraph[i++] = pTag.text();
			}
			for (Element pTag : oldPTags) {
				oldTextParagraph[k++] = pTag.text();
			}

			note.setPComments(PCommentBinding.UpdatePId(oldTextParagraph, newTextParagraph, note.getPComments()));

			note.setNoteText(noteText);
			note.setNoteTargetDate(noteTargetDate);
		} catch (Throwable e) {
			throw new Exception("노트 저장에 실패했습니다.");
		}
	}

	public void delete(long noteId) {
		noteRepository.delete(noteId);
	}

	public List<Note> readByGroupPage(Party group) {
		return noteRepository.findAllByParty(group);
	}

}
