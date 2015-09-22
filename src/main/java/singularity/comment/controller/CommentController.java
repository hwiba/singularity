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

import singularity.app.utility.RequestUtil;
import singularity.app.utility.ResponseUtil;
import singularity.comment.domain.Comment;
import singularity.comment.service.CommentService;

@Controller
@RequestMapping("/pComments")
public class CommentController {
	@Resource
	private CommentService commentService;

	@RequestMapping(value = "", method = RequestMethod.POST)
	protected ResponseEntity<Object> create(HttpSession session, Comment comment, @RequestParam long noteId)
            throws IOException, IllegalAccessException {
		if (comment.getText().equals("")) {
			return ResponseUtil.JSON("잘못된 요청입니다.", HttpStatus.PRECONDITION_FAILED);
		}
		comment = commentService.create(comment, noteId, RequestUtil.getSessionId(session));
		return ResponseUtil.JSON(comment, HttpStatus.CREATED);
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	protected ResponseEntity<Object> read(@RequestParam final Integer pageId, @RequestParam long noteId) {
		return ResponseUtil.JSON(commentService.findAllByPId(pageId, noteId), HttpStatus.OK);
	}

	@RequestMapping(value = "/{pCommentId}", method = RequestMethod.PUT)
	protected ResponseEntity<Object> update(HttpSession session, @PathVariable Long pCommentId,
			@RequestParam String commentText) throws IllegalAccessException {
        Comment comment = commentService.update(pCommentId, commentText, RequestUtil.getSessionId(session));
        return ResponseUtil.JSON(comment, HttpStatus.CREATED);
    }

	@RequestMapping(value = "/{pCommentId}", method = RequestMethod.DELETE)
	protected ResponseEntity<Object> delete(HttpSession session, @PathVariable Long pCommentId) {
		try {
			commentService.delete(pCommentId, RequestUtil.getSessionId(session));
			return ResponseUtil.JSON("코멘트가 삭제되었습니다.", HttpStatus.NO_CONTENT);
		} catch (IllegalAccessException e) {
			return ResponseUtil.JSON(e.getMessage(), HttpStatus.PRECONDITION_FAILED);
		}
	}

}
