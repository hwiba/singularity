package singularity.party.controller;

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

import singularity.common.utility.RequestUtil;
import singularity.common.utility.ResponseUtil;
import singularity.exception.FailedAddingGroupMemberException;
import singularity.exception.FailedUpdatePartyException;
import singularity.exception.PartyLeaveFailedException;
import singularity.exception.UnpermittedAccessGroupException;
import singularity.note.service.NoteService;
import singularity.party.domain.Party;
import singularity.party.service.PartyService;
import singularity.user.domain.User;
import singularity.user.dto.SessionUser;

@Controller
@RequestMapping("/party")
public class PartyController {
	@Resource
	private PartyService partyService;
	@Resource
	private NoteService noteService;

	@RequestMapping("/form")
	public String list() {
		return "party";
	}

	@RequestMapping(value = "{partyId}")
	protected String loadParty(@PathVariable Long partyId, HttpSession session, Model model) {
		model.addAttribute("party", partyService.findOne(partyId));
		model.addAttribute("admin", new SessionUser(partyService.findAdmin(partyId)));
		return "notes";
	}

	// TODO 노트 서비스 수정 후 재 수정.
//	@RequestMapping(value = "{partyId}/note/", method = RequestMethod.GET)
//	protected ResponseEntity<Object> loadNotes(@PathVariable Long partyId, HttpSession session) throws Throwable {
//		SessionUser sessionUser = RequestUtil.getSessionUser(session);
//		if (null == sessionUser) {
//			return ResponseUtil.getJSON("", HttpStatus.NOT_ACCEPTABLE);
//		}
//		Party party = partyService.findOne(partyId);
//		List<Note> notes = noteService.readByGroupPage(party);
//		for (Note note : notes) {
//			try {
//				note.setNoteText((String) new NashornEngine().markdownToHtml(note.getNoteText()));
//			} catch (IOException e) {
//				return ResponseUtil.getJSON("", HttpStatus.INTERNAL_SERVER_ERROR);
//			}
//		}
//		// TODO repository에서 가져올 때 노트의 순서를 역순으로 수정하기.
//		Collections.reverse(notes);
//
//		// TODO comment count 를 가진 dto를 만들 것인가 note가 카운트를 가지게 할 것인가 결정하기.
//		return ResponseUtil.getJSON(notes, HttpStatus.OK);
//	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	protected ResponseEntity<Object> list(HttpSession session) {
		Long userId = RequestUtil.getSessionUser(session).getId();
		return ResponseUtil.getJSON(partyService.findAllByUserId(userId), HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	protected ResponseEntity<Object> create(@RequestParam String status, @RequestParam String partyName,
			HttpSession session, Model model) {
		if (partyName.length() > 15) {
			return ResponseUtil.getJSON("그룹명은 15자 이내로 가능합니다", HttpStatus.PRECONDITION_FAILED);
		}
		Long adminId = RequestUtil.getSessionUser(session).getId();
		Party.Openness openness = "F" == status ? Party.Openness.COMMUNITY : Party.Openness.EVERYONE;
		Party party = partyService.create(partyName, adminId, openness);
		return ResponseUtil.getJSON(party, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{partyId}", method = RequestMethod.DELETE)
	protected ResponseEntity<Object> delete(@PathVariable Long partyId, HttpSession session, Model model) {
		partyService.delete(partyId, RequestUtil.getSessionUser(session).getId());
		return ResponseUtil.getJSON("", HttpStatus.OK);
	}

	@RequestMapping(value = "/{partyId}/members/invite", method = RequestMethod.POST)
	protected ResponseEntity<Object> inviteGroupMember(@PathVariable Long partyId, @RequestParam Long userId,
			HttpSession session) {
		try {
			partyService.inviteMember(RequestUtil.getSessionUser(session), userId, partyId);
		} catch (UnpermittedAccessGroupException | FailedAddingGroupMemberException e) {
			return ResponseUtil.getJSON(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return ResponseUtil.getJSON("", HttpStatus.OK);
	}

	@RequestMapping(value = "/{partyId}/members/join", method = RequestMethod.POST)
	protected ResponseEntity<Object> joinGroupMember(@PathVariable Long partyId, HttpSession session) {
		partyService.joinMember(RequestUtil.getSessionUser(session), partyId);
		return ResponseUtil.getJSON("", HttpStatus.OK);
	}

	@RequestMapping(value = "/{partyId}/members/accept", method = RequestMethod.POST)
	protected ResponseEntity<Object> acceptGroupMember(@RequestParam Long userId, @PathVariable Long partyId,
			HttpSession session) {
		partyService.addMember(userId, partyId, RequestUtil.getSessionUser(session));
		return ResponseUtil.getJSON(null, HttpStatus.ACCEPTED);
	}

	@RequestMapping(value = "/{parytId}/members/{sessionUserId}/leave", method = RequestMethod.PUT)
	protected ResponseEntity<Object> leave(@PathVariable Long sessionUserId, @PathVariable Long parytId) {
		try {
			partyService.leaveParty(sessionUserId, parytId);
		} catch (PartyLeaveFailedException e) {
			return ResponseUtil.getJSON(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
		}
		return ResponseUtil.getJSON("", HttpStatus.OK);
	}

	@RequestMapping(value = "/members/delete", method = RequestMethod.POST)
	protected ResponseEntity<Object> delete(HttpSession session, @RequestParam Long userId,
			@RequestParam Long partyId) {
		partyService.deleteMember(RequestUtil.getSessionUser(session), userId, partyId);
		return ResponseUtil.getJSON("", HttpStatus.OK);
	}

	@RequestMapping("/members/{partyId}")
	protected ResponseEntity<Object> listGroupMember(@PathVariable Long partyId) {
		List<User> members = partyService.readMembers(partyId);
		return ResponseUtil.getJSON(members, HttpStatus.OK);
	}

	@RequestMapping("/update/form/{partyId}")
	protected String updateForm(@PathVariable Long partyId, Model model, HttpSession session) {
		Party party = partyService.findOne(partyId);
		Long sessionUserId = RequestUtil.getSessionUser(session).getId();
		if (!sessionUserId.equals(party.getAdmin().getId())) {
			throw new FailedUpdatePartyException("그룹장만이 그룹설정이 가능합니다.");
		}
		model.addAttribute("party", party);
		model.addAttribute("members", partyService.readMembers(partyId));
		return "updateGroup";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	protected String updateUser(@RequestParam MultipartFile backgroundImage, HttpSession session, Party party) {
		if (party.getName().equals("")) {
			throw new FailedUpdatePartyException("그룹명이 공백입니다.");
		}
		String rootPath = session.getServletContext().getRealPath("/");
		partyService.update(RequestUtil.getSessionUser(session), party, rootPath, backgroundImage);
		return "redirect:/g/" + party.getId().toString();
	}
}
