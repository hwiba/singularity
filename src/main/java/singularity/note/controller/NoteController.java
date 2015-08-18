package singularity.note.controller;

import java.io.IOException;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import singularity.domain.Note;
import singularity.note.service.NoteService;
import singularity.party.service.PartyService;
import singularity.user.service.UserService;
import singularity.common.utility.NashornEngine;
import singularity.common.utility.ResponseUtil;
import singularity.common.utility.ServletRequestUtil;

@Controller
public class NoteController {
	@Resource
	private NoteService noteService;
	@Resource
	private PartyService partyService;
	@Resource
	private UserService userService;

	@RequestMapping("/notes/{noteId}")
	protected ResponseEntity<Object> show(@PathVariable long noteId) throws IOException, Throwable {
		Note note = noteService.findOne(noteId);
		note.setNoteText((String) new NashornEngine().markdownToHtml(note.getNoteText()));
		return ResponseUtil.getJSON(note, HttpStatus.OK);
	}

	@RequestMapping(value = "/notes", method = RequestMethod.POST)
	protected String create(@RequestParam String tempNoteId, @RequestParam String noteText,
			@RequestParam Date noteTargetDate, @RequestParam String partyId, HttpSession session, Model model)
					throws IOException {
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		if (noteText.equals("")) {
			return "redirect:/notes/editor/groups/" + partyId;
		}
		noteService.create(sessionUserId, partyId, noteText, noteTargetDate, tempNoteId);
		return "redirect:/groups/" + partyId;
	}

	@RequestMapping(value = "/notes", method = RequestMethod.PUT)
	private String update(@RequestParam String partyId, @RequestParam long noteId, @RequestParam Date noteTargetDate,
			@RequestParam String noteText) {
		try {
			noteService.update(noteId, noteText, noteTargetDate);
		} catch (Throwable e) {
			//XXX 실패 시 대응 하기.
		}
		return "redirect:/g/" + partyId;
	}

	@RequestMapping(value = "/notes/{noteId}", method = RequestMethod.DELETE)
	protected ResponseEntity<Object> delete(@PathVariable long noteId) {
		if (null == noteService.findOne(noteId)) {
			return ResponseUtil.getJSON("", HttpStatus.BAD_REQUEST);
		}
		noteService.delete(noteId);
		return ResponseUtil.getJSON("", HttpStatus.OK);
	}

	@RequestMapping("/notes/editor/groups/{partyId}")
	private String createForm(@PathVariable String partyId, Model model, HttpSession session) throws IOException {
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		Party party = partyService.findOne(partyId);
		if (!party.hasUser(userService.findOne(sessionUserId))) {
			return "redirect:/g/" + partyId;
		}
		model.addAttribute("party", party);
		return "editor";
	}

	@RequestMapping("/notes/editor/{noteId}")
	private String updateEditor(@PathVariable long noteId, Model model, HttpSession session) throws Exception {
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		Note note = noteService.findOne(noteId);
		if (!sessionUserId.equals(note.getUser().getId())) {
			throw new Exception("노트 작성자, 수정자 불일치 예외.");
		}
		model.addAttribute("note", note);
		model.addAttribute("party", partyService.findOne(note.getParty().getPartyId()));
		return "editor";
	}

}
