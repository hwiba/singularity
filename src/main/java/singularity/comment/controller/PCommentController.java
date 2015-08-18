package singularity.comment.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import singularity.user.dto.SessionUser;
import singularity.exception.UnpermittedAccessPCommentException;
import singularity.comment.service.PCommentService;
import singularity.common.utility.ResponseUtil;

@Controller
@RequestMapping("/pComments")
public class PCommentController {
	@Resource
	private PCommentService pCommentService;

	@RequestMapping(value = "", method = RequestMethod.POST)
	protected ResponseEntity<Object> create(HttpSession session, PComment pComment, @RequestParam long noteId)
			throws IOException {
		if (pComment.getPCommentText().equals("")) {
			return ResponseUtil.getJSON("잘못된 요청입니다.", HttpStatus.PRECONDITION_FAILED);
		}
		pComment = pCommentService.create(pComment, noteId, (SessionUser) session.getAttribute("sessionUser"));
		return ResponseUtil.getJSON(pComment, HttpStatus.CREATED);
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	protected ResponseEntity<Object> read(@RequestParam String pId, @RequestParam long noteId) {
		int paragraphId = Integer.parseInt(pId.replace("pId-", ""));
		return ResponseUtil.getJSON(pCommentService.findAllByPId(paragraphId, noteId), HttpStatus.OK);
	}

	@RequestMapping(value = "/{pCommentId}", method = RequestMethod.PUT)
	protected ResponseEntity<Object> update(HttpSession session, @PathVariable String pCommentId,
			@RequestParam String commentText) {
		SessionUser sessionUser = (SessionUser) session.getAttribute("sessionUser");
		try {
			PComment pComment = pCommentService.update(pCommentId, commentText, sessionUser);
			return ResponseUtil.getJSON(pComment, HttpStatus.CREATED);
		} catch (UnpermittedAccessPCommentException e) {
			return ResponseUtil.getJSON("수정할 권한이 없습니다.", HttpStatus.PRECONDITION_FAILED);
		}
	}

	@RequestMapping(value = "/{pCommentId}", method = RequestMethod.DELETE)
	protected ResponseEntity<Object> delete(HttpSession session, @PathVariable String pCommentId) {
		SessionUser sessionUser = (SessionUser) session.getAttribute("sessionUser");
		try {
			pCommentService.delete(pCommentId, sessionUser);
			return ResponseUtil.getJSON("코멘트가 삭제되었습니다.", HttpStatus.NO_CONTENT);
		} catch (UnpermittedAccessPCommentException e) {
			return ResponseUtil.getJSON(e.getMessage(), HttpStatus.PRECONDITION_FAILED);
		}
	}

	@RequestMapping(value = "/readCountByP", method = RequestMethod.GET)
	protected ResponseEntity<Object> readCountByP(@RequestParam long noteId) {
		//XXX pc 카운트 쿼리 만들기
		//pCommentService.countAllByNoteByP(noteId);
		return ResponseUtil.getJSON("{pId-1:1}", HttpStatus.OK);
	}
}
