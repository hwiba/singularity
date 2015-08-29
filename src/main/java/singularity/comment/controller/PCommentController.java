package singularity.comment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import singularity.comment.service.PCommentService;

import javax.annotation.Resource;

@Controller
@RequestMapping("/pComments")
public class PCommentController {
	@Resource
	private PCommentService pCommentService;

//	@RequestMapping(value = "", method = RequestMethod.POST)
//	protected ResponseEntity<Object> create(HttpSession session, PComment pComment, @RequestParam long noteId)
//			throws IOException {
//		if (pComment.getPCommentText().equals("")) {
//			return ResponseUtil.JSON("잘못된 요청입니다.", HttpStatus.PRECONDITION_FAILED);
//		}
//		pComment = pCommentService.create(pComment, noteId, (SessionUser) session.getAttribute("sessionUser"));
//		return ResponseUtil.JSON(pComment, HttpStatus.CREATED);
//	}
//
//	@RequestMapping(value = "", method = RequestMethod.GET)
//	protected ResponseEntity<Object> read(@RequestParam String pId, @RequestParam long noteId) {
//		int paragraphId = Integer.parseInt(pId.replace("pId-", ""));
//		return ResponseUtil.JSON(pCommentService.findAllByPId(paragraphId, noteId), HttpStatus.OK);
//	}
//
//	@RequestMapping(value = "/{pCommentId}", method = RequestMethod.PUT)
//	protected ResponseEntity<Object> update(HttpSession session, @PathVariable String pCommentId,
//			@RequestParam String commentText) {
//		SessionUser sessionUser = (SessionUser) session.getAttribute("sessionUser");
//		try {
//			PComment pComment = pCommentService.update(pCommentId, commentText, sessionUser);
//			return ResponseUtil.JSON(pComment, HttpStatus.CREATED);
//		} catch (UnpermittedAccessPCommentException e) {
//			return ResponseUtil.JSON("수정할 권한이 없습니다.", HttpStatus.PRECONDITION_FAILED);
//		}
//	}
//
//	@RequestMapping(value = "/{pCommentId}", method = RequestMethod.DELETE)
//	protected ResponseEntity<Object> delete(HttpSession session, @PathVariable String pCommentId) {
//		SessionUser sessionUser = (SessionUser) session.getAttribute("sessionUser");
//		try {
//			pCommentService.delete(pCommentId, sessionUser);
//			return ResponseUtil.JSON("코멘트가 삭제되었습니다.", HttpStatus.NO_CONTENT);
//		} catch (UnpermittedAccessPCommentException e) {
//			return ResponseUtil.JSON(e.getMessage(), HttpStatus.PRECONDITION_FAILED);
//		}
//	}
//
//	@RequestMapping(value = "/readCountByP", method = RequestMethod.GET)
//	protected ResponseEntity<Object> readCountByP(@RequestParam long noteId) {
//		//XXX pc 카운트 쿼리 만들기
//		//pCommentService.countAllByNoteByP(noteId);
//		return ResponseUtil.JSON("{pId-1:1}", HttpStatus.OK);
//	}
}
