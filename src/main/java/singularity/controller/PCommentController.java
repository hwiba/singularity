package singularity.controller;

import java.io.IOException;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import singularity.domain.Note;
import singularity.domain.PComment;
import singularity.domain.User;
import singularity.dto.out.SessionUser;
import singularity.exception.UnpermittedAccessPCommentException;
import singularity.service.PCommentService;
import singularity.utility.JSONResponseUtil;

@Controller
@RequestMapping("/pComments")
public class PCommentController {
	@Resource
	private PCommentService pCommentService;

	@RequestMapping(value = "", method = RequestMethod.POST)
	protected ResponseEntity<Object> create(HttpSession session, @RequestParam int pId,
			@RequestParam int sameSenCount, @RequestParam int sameSenIndex, @RequestParam String pCommentText,
			@RequestParam String selectedText, @RequestParam long noteId) throws IOException{
		String userId = ((SessionUser) session.getAttribute("sessionUser")).getId();
		if (pCommentText.equals("")) {
			return JSONResponseUtil.getJSONResponse("잘못된 요청입니다.", HttpStatus.PRECONDITION_FAILED);
		}
		User user = new User();
		user.setId(userId);
		Note note = new Note();
		note.setNoteId(noteId);
		PComment pComment = new PComment("", new Date(), pId, sameSenCount, sameSenIndex, pCommentText, selectedText, user, note);
		return JSONResponseUtil.getJSONResponse(pCommentService.create(pComment), HttpStatus.CREATED);
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	protected ResponseEntity<Object> list(@RequestParam String pId, @RequestParam long noteId) {
		return JSONResponseUtil.getJSONResponse(pCommentService.listByPAndNote(Integer.parseInt(pId.replace("pId-", "")), noteId), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/readCountByP", method = RequestMethod.GET)
	protected ResponseEntity<Object> readCountByP(@RequestParam long noteId) {
		return JSONResponseUtil.getJSONResponse(pCommentService.countAllByNoteByP(noteId), HttpStatus.OK);
	}

	@RequestMapping(value = "/{pCommentId}", method = RequestMethod.PUT)
	protected ResponseEntity<Object> update(HttpSession session, @PathVariable String pCommentId, @RequestParam String commentText) {
		SessionUser sessionUser = (SessionUser) session.getAttribute("sessionUser");
		try {
			return JSONResponseUtil.getJSONResponse((PComment) pCommentService.update(pCommentId, commentText, sessionUser), HttpStatus.CREATED);
		} catch (UnpermittedAccessPCommentException e) {
			return JSONResponseUtil.getJSONResponse("수정할 권한이 없습니다.", HttpStatus.PRECONDITION_FAILED);
		}
	}

	@RequestMapping(value = "/{pCommentId}", method = RequestMethod.DELETE)
	protected ResponseEntity<Object> delete(HttpSession session, @PathVariable String pCommentId) {
		SessionUser sessionUser = (SessionUser) session.getAttribute("sessionUser");
		try {
			pCommentService.delete(pCommentId, sessionUser);
			return JSONResponseUtil.getJSONResponse("코멘트가 삭제되었습니다.", HttpStatus.NO_CONTENT);
		} catch (UnpermittedAccessPCommentException e) {
			return JSONResponseUtil.getJSONResponse(e.getMessage(), HttpStatus.PRECONDITION_FAILED);
		}
	}
}
