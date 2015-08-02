package singularity.controller;

import java.io.IOException;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import singularity.domain.Note;
import singularity.exception.UnpermittedAccessGroupException;
import singularity.service.PartyService;
import singularity.service.NoteService;
import singularity.utility.DateTimeUtil;
import singularity.utility.JSONResponseUtil;
import singularity.utility.JsonResult;
import singularity.utility.Markdown;
import singularity.utility.ServletRequestUtil;


@Controller
public class NoteController {
	@Resource
	private NoteService noteService;
	@Resource
	private PartyService partyService;
//	@Resource
//	private PreviewService previewService;
//	@Resource
//	private PCommentService pCommentService;
//	@Resource
//	private TempNoteService tempNoteService;
	

//	@RequestMapping("/notes/reload")
//	protected ResponseEntity<Object> reloadNotes(@RequestParam String partyId, @RequestParam String noteTargetDate) {
//		if (partyId == null) {
//			throw new UnpermittedAccessGroupException();
//		}
//		if ("undefined".equals(noteTargetDate)) {
//			noteTargetDate = null;
//		}
//		return JSONResponseUtil.getJSONResponse(noteService.reloadPreviews(partyId, noteTargetDate), HttpStatus.OK);
//	}

	@RequestMapping("/notes/{noteId}")
	protected ResponseEntity<Object> show(@PathVariable long noteId, HttpSession session) throws IOException{
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		Note note = noteService.read(noteId);
		note.setNoteText(new Markdown().toHTML(note.getNoteText()));
		return JSONResponseUtil.getJSONResponse(note, HttpStatus.OK);
	}

	@RequestMapping(value = "/notes", method = RequestMethod.POST)
	protected String create(@RequestParam String tempNoteId, @RequestParam String noteText, @RequestParam String noteTargetDate, @RequestParam String partyId, HttpSession session, Model model) throws IOException {
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		if (noteText.equals("")) {
			return "redirect:/notes/editor/groups/" + partyId;
		}
		noteService.create(sessionUserId, partyId, noteText, DateTimeUtil.addCurrentTime(noteTargetDate), tempNoteId);
		return "redirect:/groups/" + partyId;
	}

	@RequestMapping(value = "/notes", method = RequestMethod.PUT)
	private String update(@RequestParam String partyId, @RequestParam long noteId, @RequestParam Date noteTargetDate, @RequestParam String noteText, HttpSession session) throws Exception {
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		Note note = noteService.read(noteId);
		if(!sessionUserId.equals(note.getUser().getId())){
			throw new Exception("불일치");
		}
		String editedNoteTextToMarkdown = new Markdown().toHTML(noteText);
		String originNoteTextToMarkdown = new Markdown().toHTML(note.getNoteText());

		Document editedDoc = Jsoup.parse(editedNoteTextToMarkdown);
		Document originDoc = Jsoup.parse(originNoteTextToMarkdown);

		Elements editedpTags = editedDoc.getElementsByClass("pCommentText");
		Elements originpTags = originDoc.getElementsByClass("pCommentText");

		String[] editedTextParagraph = new String[editedpTags.size()];
		String[] originTextParagraph = new String[originpTags.size()];

		int i = 0, k = 0;
		for (Element pTag : editedpTags) {
			editedTextParagraph[i++] = pTag.text();
		}
		for (Element pTag : originpTags) {
			originTextParagraph[k++] = pTag.text();
		}
		//List<Map<String, Object>> pCommentList = pCommentService.listByNoteId( noteId);
		//pCommentList = ReconnectPComments.UpdateParagraphId(originTextParagraph, editedTextParagraph, pCommentList);
		//noteService.update(noteText, noteId, DateTimeUtil.addCurrentTime(noteTargetDate), pCommentList);
		return "redirect:/g/" + partyId;
	}

	@RequestMapping(value = "/notes/{noteId}", method = RequestMethod.DELETE)
	protected ResponseEntity<Object> delete(@PathVariable long noteId) {
		if (null == noteService.read(noteId)) {
			return JSONResponseUtil.getJSONResponse("", HttpStatus.BAD_REQUEST);
		}
		noteService.delete(noteId);
		return JSONResponseUtil.getJSONResponse("", HttpStatus.OK);
	}

	@RequestMapping("/notes/editor/groups/{partyId}")
	private String createForm(@PathVariable String partyId, Model model, HttpSession session) throws IOException {
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		//noteService.checkJoinedGroup(partyId, sessionUserId);
		model.addAttribute("party", partyService.findOne(partyId));
		//model.addAttribute("tempNotes", new Gson().toJson(tempNoteService.read(sessionUserId)));
		return "editor";
	}

	@RequestMapping("/notes/editor/{noteId}")
	private String updateEditor(@PathVariable long noteId, Model model, HttpSession session) throws Exception {
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		Note note = noteService.read(noteId);
		if(!sessionUserId.equals(note.getUser().getId())){
			throw new Exception("노트 작성자, 수정자 불일치 예외.");
		}
		model.addAttribute("note", note);
		model.addAttribute("party", partyService.findOne(note.getParty().getPartyId()));
		//model.addAttribute("tempNotes", new Gson().toJson(tempNoteService.read(note.getUser().getId())));
		return "editor";
	}

	@RequestMapping(value = "/notes/editor/preview", method = RequestMethod.POST)
	private @ResponseBody JsonResult preview(@RequestParam String markdown) throws IOException {
		String html = new Markdown().toHTML(markdown);
		return new JsonResult().setSuccess(true).setMessage(html);
	}
	
//	@RequestMapping(value = "/notes/getNullDay/{partyId}/{lastDate}")
//	private @ResponseBody JsonResult readNullDay(@PathVariable String partyId, @PathVariable String lastDate) throws IOException, ParseException {
//		return new JsonResult().setSuccess(true).setObjectValues(noteService.readNullDay(partyId, lastDate));
//	}
//	
//	@RequestMapping(value = "/notes/temp", method = RequestMethod.POST)
//	private @ResponseBody JsonResult tempNoteCreate(@RequestParam String noteText,
//			@RequestParam String createDate, HttpSession session) throws IOException {
//		logger.debug("noteText : {}, createDate : {}", noteText, createDate);
//		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
//		long tempNoteId = tempNoteService.create(noteText, DateTimeUtil.addCurrentTime(createDate), sessionUserId);
//		return new JsonResult().setSuccess(true).setObject(tempNoteId);
//	}
//	
//	@RequestMapping("/notes/temp/{noteId}")
//	protected @ResponseBody JsonResult<Preview> tempNoteRead(@PathVariable long noteId) {
//		logger.debug("noteId:{}", noteId);
//		return new JsonResult().setSuccess(true).setObject(tempNoteService.readByNoteId(noteId));
//	}
//	
//	@RequestMapping(value = "/notes/temp", method = RequestMethod.PUT)
//	protected @ResponseBody JsonResult<Preview> tempNoteUpdate(@RequestParam long noteId, @RequestParam String noteText, 
//			@RequestParam String createDate) {
//		logger.debug("noteId:{}", noteId);
//		
//		
//		if(tempNoteService.update(noteId, noteText, DateTimeUtil.addCurrentTime(createDate))) {
//			return new JsonResult().setSuccess(true).setObject(tempNoteService.readByNoteId(noteId));
//		}
//		return new JsonResult().setSuccess(false);
//	}
//	
//	@RequestMapping(value = "/notes/temp/{noteId}", method = RequestMethod.DELETE)
//	protected @ResponseBody JsonResult<Preview> tempNotedelete(@PathVariable long noteId) {
//		logger.debug("noteId:{}", noteId);
//		if(tempNoteService.delete(noteId)) {
//			return new JsonResult().setSuccess(true);
//		}
//		return new JsonResult().setSuccess(false);
//	}
}
