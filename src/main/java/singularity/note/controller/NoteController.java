package singularity.note.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import singularity.app.utility.NashornEngine;
import singularity.app.utility.RequestUtil;
import singularity.app.utility.ResponseUtil;
import singularity.note.domain.Note;
import singularity.note.service.NoteService;
import singularity.party.domain.Party;
import singularity.party.service.PartyService;
import singularity.user.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Controller
public class NoteController {
	@Resource
	private NoteService noteService;
	@Resource
	private PartyService partyService;
	@Resource
	private UserService userService;

	@RequestMapping("/note/{noteId}")
	ResponseEntity<Object> show(@PathVariable long noteId) throws IOException, Throwable {
		Note note = noteService.findOne(noteId);
		note.setText((String) new NashornEngine().markdownToHtml(note.getText()));
		return ResponseUtil.JSON(note, HttpStatus.OK);
	}

	@RequestMapping(value = "/{partyId}/note/", method = RequestMethod.GET)
    ResponseEntity<Object> loadNotes(@PathVariable Long partyId, HttpSession session) throws Throwable {
		Long sessionId = RequestUtil.getSessionId(session);
		if (null == sessionId) {
			return ResponseUtil.JSON("", HttpStatus.NOT_ACCEPTABLE);
		}
		Party party = partyService.findOne(partyId);
		List<Note> notes = noteService.readByGroupPage(party);
		for (Note note : notes) {
			try {
				note.setText((String) new NashornEngine().markdownToHtml(note.getText()));
			} catch (IOException e) {
				return ResponseUtil.JSON("", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		// TODO repository에서 가져올 때 노트의 순서를 역순으로 수정하기.
		Collections.reverse(notes);

		// TODO comment count 를 가진 dto를 만들 것인가 note가 카운트를 가지게 할 것인가 결정하기.
		return ResponseUtil.JSON(notes, HttpStatus.OK);
	}

	@RequestMapping(value = "/{partyId}/note", method = RequestMethod.POST)
	String create(@RequestParam String noteText, @PathVariable Long partyId, HttpSession session, Model model)
					throws IOException {
		Long sessionId = RequestUtil.getSessionId(session);
		if (noteText.equals("")) {
			return "redirect:/notes/editor/party/" + String.valueOf(partyId);
		}
		noteService.create(sessionId, partyId, noteText);
		return "redirect:/party/" + String.valueOf(partyId);
	}

	@RequestMapping(value = "/{partyId}/note/{noteId}", method = RequestMethod.PUT)
	private String update(@PathVariable Long partyId, @PathVariable Long noteId, @RequestParam String text) {
		try {
			noteService.update(noteId, text);
		} catch (Throwable e) {
			//TODO 실패 시 대응 하기.
		}
		return "redirect:/g/" + partyId;
	}

	@RequestMapping(value = "/note/{noteId}", method = RequestMethod.DELETE)
	protected ResponseEntity<Object> delete(@PathVariable Long noteId) {
		if (null == noteService.findOne(noteId)) {
			return ResponseUtil.JSON("", HttpStatus.BAD_REQUEST);
		}
		noteService.delete(noteId);
		return ResponseUtil.JSON("", HttpStatus.OK);
	}

	@RequestMapping("{partyId}/note/editor/")
	private String createForm(@PathVariable Long partyId, Model model, HttpSession session) throws IOException {
		Long sessionId = RequestUtil.getSessionId(session);
		Party party = partyService.findOne(partyId);
		if (!party.hasMember(userService.findOne(sessionId))) {
			return "redirect:/g/" + partyId;
		}
		model.addAttribute("party", party);
		return "editor";
	}

	@RequestMapping("/notes/editor/{noteId}")
	private String updateEditor(@PathVariable long noteId, Model model, HttpSession session) throws Exception {
        Long sessionId = RequestUtil.getSessionId(session);
		Note note = noteService.findOne(noteId);
		if (!sessionId.equals(note.getWriter().getId())) {
			throw new Exception("노트 작성자, 수정자 불일치 예외.");
		}
		model.addAttribute("note", note);
		model.addAttribute("party", partyService.findOne(note.getParty().getId()));
		return "editor";
	}

}
