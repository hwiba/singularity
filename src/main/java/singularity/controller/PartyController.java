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
import singularity.domain.User;
import singularity.dto.out.SessionUser;
import singularity.exception.FailedAddingGroupMemberException;
import singularity.exception.FailedUpdatePartyException;
import singularity.exception.GroupMemberException;
import singularity.exception.UnpermittedAccessGroupException;
import singularity.service.NoteService;
import singularity.service.PartyService;
import singularity.utility.ResponseUtil;
import singularity.utility.NashornEngine;
import singularity.utility.ServletRequestUtil;

@Controller
@RequestMapping("/groups")
public class PartyController {
	@Resource
	private PartyService partyService;
	@Resource
	private NoteService noteService;

	@RequestMapping("/form")
	public String list() {
		return "groups";
	}

	@RequestMapping(value="{partyId}")
	protected String loadGroupAndNotes(@PathVariable String partyId, HttpSession session, Model model) {
		// XXX Session이 없는데 공개 그룹일 때의 대응과 비공개 그룹일 때의 대응을 분기하기. 이것은 시큐리티의 문제인가 아닌가
		model.addAttribute("party", partyService.findOne(partyId));
		SessionUser admin = partyService.findAdminUser(partyId);
		model.addAttribute("admin", admin);
		return "notes";
	}

	@RequestMapping(value="{partyId}/note/", method=RequestMethod.GET)
	protected ResponseEntity<Object> loadNotes(@PathVariable String partyId, HttpSession session) throws Throwable {
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		if (null == sessionUserId) {
			return ResponseUtil.getJSON("", HttpStatus.NOT_ACCEPTABLE);
		}
		Party group = partyService.findOne(partyId);
		List<Note> notes = noteService.readByGroupPage(group);
		for (Note note : notes) {
			try {
				note.setNoteText((String) new NashornEngine().markdownToHtml(note.getNoteText()));
			} catch (IOException e) {
				return ResponseUtil.getJSON("", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return ResponseUtil.getJSON(notes, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	protected ResponseEntity<Object> list(HttpSession session) {
		String userId = ServletRequestUtil.getUserIdFromSession(session);
		return ResponseUtil.getJSON(partyService.findAllByUserId(userId), HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	protected ResponseEntity<Object> create(@RequestParam String status, @RequestParam String partyName,
			HttpSession session, Model model) {
		if (partyName.length() > 15)
			return ResponseUtil.getJSON("그룹명은 15자 이내로 가능합니다", HttpStatus.PRECONDITION_FAILED);
		String groupCaptainUserId = ServletRequestUtil.getUserIdFromSession(session);
		Party party = partyService.create(partyName, groupCaptainUserId, status);
		return ResponseUtil.getJSON(party, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{partyId}", method = RequestMethod.DELETE)
	protected ResponseEntity<Object> delete(@PathVariable String partyId, HttpSession session, Model model) {
		partyService.delete(partyId, ServletRequestUtil.getUserIdFromSession(session));
		return ResponseUtil.getJSON("", HttpStatus.OK);
	}

	@RequestMapping(value = "/members/invite", method = RequestMethod.POST)
	protected ResponseEntity<Object> inviteGroupMember(@RequestParam String userId, @RequestParam String partyId,
			@RequestParam String sessionUserId) {
		try {
			partyService.inviteMember(sessionUserId, userId, partyId);
		} catch(UnpermittedAccessGroupException | FailedAddingGroupMemberException e) {
			return ResponseUtil.getJSON(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return ResponseUtil.getJSON("", HttpStatus.OK);
	}

	@RequestMapping(value = "/members/accept", method = RequestMethod.POST)
	protected ResponseEntity<Object> acceptGroupMember(@RequestParam String userId, @RequestParam String partyId) {
		Party party = partyService.addMember(userId, partyId);
		return ResponseUtil.getJSON(party, HttpStatus.ACCEPTED);
	}

	@RequestMapping(value = "/members/join", method = RequestMethod.POST)
	protected ResponseEntity<Object> joinGroupMember(@RequestParam String partyId, @RequestParam String sessionUserId) {
		partyService.joinMember(sessionUserId, partyId);
		return ResponseUtil.getJSON("", HttpStatus.OK);
	}

	@RequestMapping(value = "/{parytId}/members/{sessionUserId}/leave", method = RequestMethod.PUT)
	protected ResponseEntity<Object> leave(@PathVariable String sessionUserId, @PathVariable String parytId) {
		try {
			partyService.leaveParty(sessionUserId, parytId);
		} catch (GroupMemberException e) {
			return ResponseUtil.getJSON(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
		}
		return ResponseUtil.getJSON("", HttpStatus.OK);
	}

	@RequestMapping(value = "/members/delete", method = RequestMethod.POST)
	protected ResponseEntity<Object> delete(@RequestParam String sessionUserId, @RequestParam String userId,
			@RequestParam String partyId) {
		//XXX 삭제 실패 익셉션 처리하기.
		partyService.deleteMember(sessionUserId, userId, partyId);
		return ResponseUtil.getJSON("", HttpStatus.OK);
	}

	@RequestMapping("/members/{partyId}")
	protected ResponseEntity<Object> listGroupMember(@PathVariable String partyId) {
		List<User> members = partyService.readMembers(partyId);
		return ResponseUtil.getJSON(members, HttpStatus.OK);
	}

	@RequestMapping("/update/form/{partyId}")
	protected String updateForm(@PathVariable String partyId, Model model, HttpSession session) {
		Party party = partyService.findOne(partyId);
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		if (!sessionUserId.equals(party.getAdminUser().getId())) {
			throw new FailedUpdatePartyException("그룹장만이 그룹설정이 가능합니다.");
		}
		model.addAttribute("party", party);
		model.addAttribute("members", partyService.readMembers(partyId));
		return "updateGroup";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	protected String updateUser(@RequestParam String sessionUserId,
			@RequestParam("backgroundImage") MultipartFile backgroundImage, HttpSession session, Party party) {
		if (party.getPartyName().equals("")) {
			throw new FailedUpdatePartyException("그룹명이 공백입니다.");
		}
		String rootPath = session.getServletContext().getRealPath("/");
		partyService.update(sessionUserId, party, rootPath, backgroundImage);
		return "redirect:/g/" + party.getPartyId();
	}
}
