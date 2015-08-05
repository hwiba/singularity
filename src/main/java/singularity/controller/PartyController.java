package singularity.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import singularity.exception.FailedUpdatePartyException;
import singularity.exception.GroupMemberException;
import singularity.service.NoteService;
import singularity.service.PartyService;
import singularity.utility.JSONResponseUtil;
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

	@RequestMapping("{partyId}")
	protected String loadGroupAndNotes(@PathVariable String partyId, HttpSession session, Model model) {
		// TODO Session이 없는데 공개 그룹일 때의 대응과 비공개 그룹일 때의 대응을 분기하기.
		model.addAttribute("party", partyService.findOne(partyId));
		SessionUser admin = partyService.readCaptainUser(partyId);
		model.addAttribute("admin", admin);
		return "notes";
	}

	@RequestMapping("{partyId}/note/")
	protected ResponseEntity<Object> loadNotes(@PathVariable String partyId, HttpSession session) throws Throwable {
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		if (null == sessionUserId) {
			return JSONResponseUtil.getJSONResponse("", HttpStatus.NOT_ACCEPTABLE);
		}
		Party group = partyService.findOne(partyId);
		List<Note> notes = noteService.readByGroupPage(group);
		for (Note note : notes) {
			try {
				note.setNoteText((String) new NashornEngine().markdownToHtml(note.getNoteText()));
			} catch (IOException e) {
				return JSONResponseUtil.getJSONResponse("", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return JSONResponseUtil.getJSONResponse(notes, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	protected ResponseEntity<Object> list(HttpSession session) {
		String userId = ServletRequestUtil.getUserIdFromSession(session);
		return JSONResponseUtil.getJSONResponse(partyService.findAllByUserId(userId), HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	protected ResponseEntity<Object> create(@RequestParam String status, @RequestParam String partyName,
			HttpSession session, Model model) {
		if (partyName.length() > 15)
			return JSONResponseUtil.getJSONResponse("그룹명은 15자 이내로 가능합니다", HttpStatus.PRECONDITION_FAILED);
		String groupCaptainUserId = ServletRequestUtil.getUserIdFromSession(session);
		Party party = partyService.create(partyName, groupCaptainUserId, status);
		return JSONResponseUtil.getJSONResponse(party, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{partyId}", method = RequestMethod.DELETE)
	protected ResponseEntity<Object> delete(@PathVariable String partyId, HttpSession session, Model model) {
		partyService.delete(partyId, ServletRequestUtil.getUserIdFromSession(session));
		return JSONResponseUtil.getJSONResponse("", HttpStatus.OK);
	}

	@RequestMapping(value = "/members/invite", method = RequestMethod.POST)
	protected ResponseEntity<Object> inviteGroupMember(@RequestParam String userId, @RequestParam String partyId,
			@RequestParam String sessionUserId) {
		partyService.inviteGroupMember(sessionUserId, userId, partyId);
		return JSONResponseUtil.getJSONResponse("", HttpStatus.OK);
	}

	@RequestMapping(value = "/members/accept", method = RequestMethod.POST)
	protected ResponseEntity<Object> acceptGroupMember(@RequestParam String userId, @RequestParam String partyId) {
		Party party = partyService.addMember(userId, partyId);
		return JSONResponseUtil.getJSONResponse(party, HttpStatus.ACCEPTED);
	}

	@RequestMapping(value = "/members/join", method = RequestMethod.POST)
	protected ResponseEntity<Object> joinGroupMember(@RequestParam String partyId, @RequestParam String sessionUserId) {
		partyService.joinGroupMember(sessionUserId, partyId);
		return JSONResponseUtil.getJSONResponse("", HttpStatus.OK);
	}

	@RequestMapping(value = "/members/leave", method = RequestMethod.POST)
	protected ResponseEntity<Object> leave(@RequestParam String sessionUserId, @RequestParam String parytId) {
		try {
			partyService.leaveParty(sessionUserId, parytId);
		} catch (GroupMemberException e) {
			return JSONResponseUtil.getJSONResponse(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
		}
		return JSONResponseUtil.getJSONResponse("", HttpStatus.OK);
	}

	@RequestMapping(value = "/members/delete", method = RequestMethod.POST)
	protected ResponseEntity<Object> delete(@RequestParam String sessionUserId, @RequestParam String userId,
			@RequestParam String partyId) {
		partyService.deleteMember(sessionUserId, userId, partyId);
		return JSONResponseUtil.getJSONResponse("", HttpStatus.OK);
	}

	private static final Logger logger = LoggerFactory.getLogger(PartyController.class);
	@RequestMapping("/members/{partyId}")
	protected ResponseEntity<Object> listGroupMember(@PathVariable String partyId) {
		List<User> members = partyService.readMembers(partyId);
		logger.warn("\n\nparty members = {}", members);
		return JSONResponseUtil.getJSONResponse(members, HttpStatus.OK);
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
			throw new FailedUpdatePartyException("그룹명이 공백입니다."); // 잘못된 접근
		}
		String rootPath = session.getServletContext().getRealPath("/");
		partyService.update(sessionUserId, party, rootPath, backgroundImage);
		return "redirect:/g/" + party.getPartyId();
	}
}
