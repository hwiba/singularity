package singularity.note.controller;

import org.springframework.stereotype.Controller;

@Controller
public class NoteController {
//	@Resource
//	private NoteService noteService;
//	@Resource
//	private PartyService partyService;
//	@Resource
//	private UserService userService;
//
//	@RequestMapping("/notes/{noteId}")
//	protected ResponseEntity<Object> show(@PathVariable long noteId) throws IOException, Throwable {
//		Note note = noteService.findOne(noteId);
//		note.setNoteText((String) new NashornEngine().markdownToHtml(note.getNoteText()));
//		return ResponseUtil.JSON(note, HttpStatus.OK);
//	}

    // TODO 노트 서비스 수정 후 재 수정.
//	@RequestMapping(value = "{partyId}/note/", method = RequestMethod.GET)
//	protected ResponseEntity<Object> loadNotes(@PathVariable Long partyId, HttpSession session) throws Throwable {
//		SessionUser sessionUser = RequestUtil.getSessionUser(session);
//		if (null == sessionUser) {
//			return ResponseUtil.JSON("", HttpStatus.NOT_ACCEPTABLE);
//		}
//		Party party = partyService.findOne(partyId);
//		List<Note> notes = noteService.readByGroupPage(party);
//		for (Note note : notes) {
//			try {
//				note.setNoteText((String) new NashornEngine().markdownToHtml(note.getNoteText()));
//			} catch (IOException e) {
//				return ResponseUtil.JSON("", HttpStatus.INTERNAL_SERVER_ERROR);
//			}
//		}
//		// TODO repository에서 가져올 때 노트의 순서를 역순으로 수정하기.
//		Collections.reverse(notes);
//
//		// TODO comment count 를 가진 dto를 만들 것인가 note가 카운트를 가지게 할 것인가 결정하기.
//		return ResponseUtil.JSON(notes, HttpStatus.OK);
//	}
//
//	@RequestMapping(value = "/notes", method = RequestMethod.POST)
//	protected String create(@RequestParam String tempNoteId, @RequestParam String noteText,
//			@RequestParam Date noteTargetDate, @RequestParam String partyId, HttpSession session, Model model)
//					throws IOException {
//		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
//		if (noteText.equals("")) {
//			return "redirect:/notes/editor/groups/" + partyId;
//		}
//		noteService.create(sessionUserId, partyId, noteText, noteTargetDate, tempNoteId);
//		return "redirect:/groups/" + partyId;
//	}
//
//	@RequestMapping(value = "/notes", method = RequestMethod.PUT)
//	private String update(@RequestParam String partyId, @RequestParam long noteId, @RequestParam Date noteTargetDate,
//			@RequestParam String noteText) {
//		try {
//			noteService.update(noteId, noteText, noteTargetDate);
//		} catch (Throwable e) {
//			//XXX 실패 시 대응 하기.
//		}
//		return "redirect:/g/" + partyId;
//	}
//
//	@RequestMapping(value = "/notes/{noteId}", method = RequestMethod.DELETE)
//	protected ResponseEntity<Object> delete(@PathVariable long noteId) {
//		if (null == noteService.findOne(noteId)) {
//			return ResponseUtil.JSON("", HttpStatus.BAD_REQUEST);
//		}
//		noteService.delete(noteId);
//		return ResponseUtil.JSON("", HttpStatus.OK);
//	}
//
//	@RequestMapping("/notes/editor/groups/{partyId}")
//	private String createForm(@PathVariable String partyId, Model model, HttpSession session) throws IOException {
//		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
//		Party party = partyService.findOne(partyId);
//		if (!party.hasUser(userService.findOne(sessionUserId))) {
//			return "redirect:/g/" + partyId;
//		}
//		model.addAttribute("party", party);
//		return "editor";
//	}
//
//	@RequestMapping("/notes/editor/{noteId}")
//	private String updateEditor(@PathVariable long noteId, Model model, HttpSession session) throws Exception {
//		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
//		Note note = noteService.findOne(noteId);
//		if (!sessionUserId.equals(note.getUser().getId())) {
//			throw new Exception("노트 작성자, 수정자 불일치 예외.");
//		}
//		model.addAttribute("note", note);
//		model.addAttribute("party", partyService.findOne(note.getParty().getPartyId()));
//		return "editor";
//	}

}
