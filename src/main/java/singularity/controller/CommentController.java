package singularity.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;

import singularity.domain.Comment;
import singularity.domain.Note;
import singularity.dto.out.SessionUser;
import singularity.exception.UnpermittedAccessGroupException;
import singularity.service.CommentService;
import singularity.utility.ResponseUtil;
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
			return ResponseUtil.getJSON("", HttpStatus.BAD_REQUEST);
		Comment comment = new Comment();
		comment.setCommentText(commentText);
		try {
			return ResponseUtil.getJSON(commentService.create(sessionUser, note, comment), HttpStatus.OK);
		} catch (UnpermittedAccessGroupException e) {
			return ResponseUtil.getJSON("", HttpStatus.BAD_REQUEST);
		}
	}

	@ResponseBody
	@RequestMapping("/{noteId}")
	protected JsonResult list(@PathVariable long noteId) {
		return new JsonResult().setSuccess(true).setObject(commentService.findAll(noteId));
	}

	@ResponseBody
	@RequestMapping(value = "/{commentId}", method = RequestMethod.PUT)
	protected JsonResult update(@PathVariable long commentId, @RequestParam String commentText) {
		return new JsonResult().setSuccess(true).setObject(commentService.update(commentId, commentText));
	}

	@ResponseBody
	@RequestMapping(value = "/{commentId}", method = RequestMethod.DELETE)
	protected JsonResult delete(@PathVariable long commentId) {
		commentService.delete(commentId);
		return new JsonResult().setSuccess(true);
	}
}
