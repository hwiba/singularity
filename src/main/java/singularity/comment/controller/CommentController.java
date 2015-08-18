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
import org.springframework.web.bind.annotation.ResponseBody;

import singularity.domain.Note;
import singularity.user.dto.SessionUser;
import singularity.exception.UnpermittedAccessGroupException;
import singularity.comment.service.CommentService;
import singularity.common.utility.ResponseUtil;

@Controller
@RequestMapping("/comments")
public class CommentController {
	
	@Resource
	private CommentService commentService;

	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.POST)
	protected ResponseEntity<Object> create(HttpSession session, @RequestParam String commentText, @RequestParam long noteId) throws IOException{
		SessionUser sessionUser = (SessionUser)session.getAttribute("sessionUser");
		Note note = new Note();
		note.setNoteId(noteId);
		if (commentText.equals("")) {
			return ResponseUtil.getJSON("", HttpStatus.BAD_REQUEST);
		}
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
	protected ResponseEntity<Object> findAll(@PathVariable long noteId) {
		return ResponseUtil.getJSON(commentService.findAll(noteId), HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(value = "/{commentId}", method = RequestMethod.PUT)
	protected ResponseEntity<Object> update(@PathVariable long commentId, @RequestParam String commentText) {
		return ResponseUtil.getJSON(commentService.update(commentId, commentText), HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(value = "/{commentId}", method = RequestMethod.DELETE)
	protected ResponseEntity<Object> delete(@PathVariable long commentId) {
		commentService.delete(commentId);
		return ResponseUtil.getJSON("", HttpStatus.OK);
	}
}
