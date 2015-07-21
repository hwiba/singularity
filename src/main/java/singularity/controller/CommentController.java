package singularity.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import singularity.domain.Comment;
import singularity.domain.Note;
import singularity.dto.out.SessionUser;
import singularity.exception.UnpermittedAccessGroupException;
import singularity.service.CommentService;
import singularity.utility.JSONResponseUtil;
import singularity.utility.JsonResult;

@Controller
@RequestMapping("/comments")
public class CommentController {
	
	@Resource
	private CommentService commentService;

	@RequestMapping(value = "", method = RequestMethod.POST)
	protected @ResponseBody ResponseEntity<Object> create(HttpSession session, @RequestParam String commentText, @RequestParam long noteId) throws IOException{
		SessionUser sessionUser = (SessionUser)session.getAttribute("sessionUser");
		Note note = new Note();
		note.setNoteId(noteId);
		if (commentText.equals(""))
			return JSONResponseUtil.getJSONResponse("", HttpStatus.BAD_REQUEST);
		Comment comment = new Comment();
		comment.setCommentText(commentText);
		try {
			return JSONResponseUtil.getJSONResponse(commentService.create(sessionUser, note, comment), HttpStatus.OK);
		} catch (UnpermittedAccessGroupException e) {
			return JSONResponseUtil.getJSONResponse("", HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping("/{noteId}")
	protected @ResponseBody JsonResult list(@PathVariable long noteId) {
		return new JsonResult().setSuccess(true).setObject(commentService.list(noteId));
	}

	@RequestMapping(value = "/{commentId}", method = RequestMethod.PUT)
	protected @ResponseBody JsonResult update(@PathVariable long commentId, @RequestParam String commentText) {
		return new JsonResult().setSuccess(true).setObject(commentService.update(commentId, commentText));
	}

	@RequestMapping(value = "/{commentId}", method = RequestMethod.DELETE)
	protected @ResponseBody JsonResult delete(@PathVariable long commentId) {
		commentService.delete(commentId);
		return new JsonResult().setSuccess(true);
	}
}
