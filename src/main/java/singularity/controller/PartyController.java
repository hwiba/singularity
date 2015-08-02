package singularity.controller;

import java.io.IOException;
import java.util.List;

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
import org.springframework.web.multipart.MultipartFile;

import singularity.domain.Note;
import singularity.domain.Party;
import singularity.dto.out.SessionUser;
import singularity.exception.FailedUpdatePartyException;
import singularity.exception.GroupMemberException;
import singularity.service.NoteService;
import singularity.service.PartyService;
import singularity.utility.JSONResponseUtil;
import singularity.utility.Markdown;
import singularity.utility.ServletRequestUtil;

@Controller
@RequestMapping("/groups")
public class PartyController {
	@Resource
	private PartyService groupService;
	@Resource
	private NoteService noteService;

	@RequestMapping("/form")
	public String list() {
		return "groups";
	}

	@RequestMapping("{groupId}")
	protected String loadGroupAndNotes(@PathVariable String groupId, HttpSession session, Model model) {
		// TODO Session이 없는데 공개 그룹일 때의 대응과 비공개 그룹일 때의 대응을 분기하기.
		model.addAttribute("group", groupService.findOne(groupId));
		SessionUser admin = groupService.readCaptainUser(groupId);
		model.addAttribute("admin", admin);
		return "notes";
	}

	@RequestMapping("{groupId}/note/")
	protected ResponseEntity<Object> loadNotes(@PathVariable String groupId, HttpSession session) {
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		if (null == sessionUserId) {
			return JSONResponseUtil.getJSONResponse("", HttpStatus.NOT_ACCEPTABLE);
		}
		Party group = groupService.findOne(groupId);
		List<Note> notes = noteService.readByGroupPage(group);
		for (Note note : notes) {
			try {
				note.setNoteText(new Markdown().toHTML(note.getNoteText()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return JSONResponseUtil.getJSONResponse(notes, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	protected ResponseEntity<Object> list(HttpSession session) {
		String userId = ServletRequestUtil.getUserIdFromSession(session);
		return JSONResponseUtil.getJSONResponse(groupService.readGroups(userId), HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	protected ResponseEntity<Object> create(@RequestParam String status, @RequestParam String groupName,
			HttpSession session, Model model) {
		if (groupName.length() > 15)
			return JSONResponseUtil.getJSONResponse("그룹명은 15자 이내로 가능합니다", HttpStatus.PRECONDITION_FAILED);
		String groupCaptainUserId = ServletRequestUtil.getUserIdFromSession(session);
		Party group = groupService.create(groupName, groupCaptainUserId, status);
		return JSONResponseUtil.getJSONResponse(group, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{groupId}", method = RequestMethod.DELETE)
	protected ResponseEntity<Object> delete(@PathVariable String groupId, HttpSession session, Model model) {
		groupService.delete(groupId, ServletRequestUtil.getUserIdFromSession(session));
		return JSONResponseUtil.getJSONResponse("", HttpStatus.OK);
	}

	@RequestMapping(value = "/members/invite", method = RequestMethod.POST)
	protected ResponseEntity<Object> inviteGroupMember(@RequestParam String userId, @RequestParam String groupId,
			@RequestParam String sessionUserId) {
		groupService.inviteGroupMember(sessionUserId, userId, groupId);
		return JSONResponseUtil.getJSONResponse("", HttpStatus.OK);
	}

	@RequestMapping(value = "/members/accept", method = RequestMethod.POST)
	protected ResponseEntity<Object> acceptGroupMember(@RequestParam String userId, @RequestParam String groupId) {
		Party group = groupService.addMember(userId, groupId);
		return JSONResponseUtil.getJSONResponse(group, HttpStatus.ACCEPTED);
	}

	@RequestMapping(value = "/members/join", method = RequestMethod.POST)
	protected ResponseEntity<Object> joinGroupMember(@RequestParam String groupId, @RequestParam String sessionUserId) {
		groupService.joinGroupMember(sessionUserId, groupId);
		return JSONResponseUtil.getJSONResponse("", HttpStatus.OK);
	}

	@RequestMapping(value = "/members/leave", method = RequestMethod.POST)
	protected ResponseEntity<Object> leave(@RequestParam String sessionUserId, @RequestParam String groupId) {
		try {
			groupService.leaveParty(sessionUserId, groupId);
		} catch (GroupMemberException e) {
			return JSONResponseUtil.getJSONResponse(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
		}
		return JSONResponseUtil.getJSONResponse("", HttpStatus.OK);
	}

	@RequestMapping(value = "/members/delete", method = RequestMethod.POST)
	protected ResponseEntity<Object> delete(@RequestParam String sessionUserId, @RequestParam String userId,
			@RequestParam String groupId) {
		groupService.deleteMember(sessionUserId, userId, groupId);
		return JSONResponseUtil.getJSONResponse("", HttpStatus.OK);
	}

	@RequestMapping("/members/{groupId}")
	protected ResponseEntity<Object> listGroupMember(@PathVariable String groupId) {
		return JSONResponseUtil.getJSONResponse(groupService.readMembers(groupId), HttpStatus.OK);
	}

	@RequestMapping("/update/form/{groupId}")
	protected String updateForm(@PathVariable String groupId, Model model, HttpSession session) {
		Party group = groupService.findOne(groupId);
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		if (!sessionUserId.equals(group.getAdminUser().getId())) {
			throw new FailedUpdatePartyException("그룹장만이 그룹설정이 가능합니다.");
		}
		model.addAttribute("group", group);
		model.addAttribute("members", groupService.readMembers(groupId));
		return "updateGroup";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	protected String updateUser(@RequestParam String sessionUserId,
			@RequestParam("backgroundImage") MultipartFile backgroundImage, HttpSession session, Party group) {
		if (group.getPartyName().equals("")) {
			throw new FailedUpdatePartyException("그룹명이 공백입니다."); // 잘못된 접근
		}
		String rootPath = session.getServletContext().getRealPath("/");
		groupService.update(sessionUserId, group, rootPath, backgroundImage);
		return "redirect:/g/" + group.getPartyId();
	}
}
