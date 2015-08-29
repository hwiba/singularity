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
import singularity.party.domain.Party;
import singularity.party.service.PartyService;
import singularity.user.domain.User;

@Controller
@RequestMapping("/party")
public class PartyController {
	@Resource
	private PartyService partyService;

	@RequestMapping("/form")
	public String show() {
		return "party";
	}

	@RequestMapping(value = "/{partyId}")
	protected String getParty(@PathVariable final Long partyId, Model model) {
		model.addAttribute("party", partyService.findOne(partyId));
		return "notes";
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	protected ResponseEntity<Object> getAccessPartys(HttpSession session) {
		Long userId = RequestUtil.getSessionUser(session).getId();
		return ResponseUtil.JSON(partyService.findAllByUserId(userId), HttpStatus.OK);
	}

    //TODO party 생성 시 DTO로 받기.
	@RequestMapping(value = "", method = RequestMethod.POST)
	protected ResponseEntity<Object> create(@RequestParam final String status, @RequestParam final String partyName,
			HttpSession session, Model model) {
		if (partyName.length() > 15) {
			return ResponseUtil.JSON("그룹명은 15자 이내로 가능합니다", HttpStatus.PRECONDITION_FAILED);
		}
		final Long adminId = RequestUtil.getSessionUser(session).getId();
		final Party.Openness openness = "F" == status ? Party.Openness.COMMUNITY : Party.Openness.EVERYONE;
		final Party party = partyService.create(partyName, adminId, openness);
		return ResponseUtil.JSON(party, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{partyId}", method = RequestMethod.DELETE)
	protected ResponseEntity<Object> delete(@PathVariable final Long partyId, HttpSession session, Model model) {
        try {
            partyService.delete(partyId, RequestUtil.getSessionUser(session).getId());
        } catch (IllegalArgumentException e) {
            return ResponseUtil.JSON(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
		return ResponseUtil.JSON("", HttpStatus.OK);
	}

	@RequestMapping(value = "/{partyId}/members/invite", method = RequestMethod.POST)
	protected ResponseEntity<Object> inviteGroupMember(@PathVariable Long partyId, @RequestParam Long userId,
			HttpSession session) {
		try {
			partyService.inviteMember(RequestUtil.getSessionUser(session), userId, partyId);
		} catch (UnpermittedAccessGroupException | FailedAddingGroupMemberException e) {
			return ResponseUtil.JSON(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return ResponseUtil.JSON("", HttpStatus.OK);
	}

	@RequestMapping(value = "/{partyId}/members/join", method = RequestMethod.POST)
	protected ResponseEntity<Object> joinGroupMember(@PathVariable Long partyId, HttpSession session) {
		partyService.joinMember(RequestUtil.getSessionUser(session), partyId);
		return ResponseUtil.JSON("", HttpStatus.OK);
	}

	@RequestMapping(value = "/{partyId}/members/accept", method = RequestMethod.POST)
	protected ResponseEntity<Object> acceptGroupMember(@RequestParam Long userId, @PathVariable Long partyId,
			HttpSession session) {
		partyService.addMember(userId, partyId, RequestUtil.getSessionUser(session));
		return ResponseUtil.JSON(null, HttpStatus.ACCEPTED);
	}

	@RequestMapping(value = "/{parytId}/members/{sessionUserId}/leave", method = RequestMethod.PUT)
	protected ResponseEntity<Object> leave(@PathVariable Long sessionUserId, @PathVariable Long parytId) {
		try {
			partyService.leaveParty(sessionUserId, parytId);
		} catch (PartyLeaveFailedException e) {
			return ResponseUtil.JSON(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
		}
		return ResponseUtil.JSON("", HttpStatus.OK);
	}

	@RequestMapping(value = "/members/delete", method = RequestMethod.POST)
	protected ResponseEntity<Object> delete(HttpSession session, @RequestParam Long userId,
			@RequestParam Long partyId) {
		partyService.deleteMember(RequestUtil.getSessionUser(session), userId, partyId);
		return ResponseUtil.JSON("", HttpStatus.OK);
	}

	@RequestMapping("/members/{partyId}")
	protected ResponseEntity<Object> listGroupMember(@PathVariable Long partyId) {
		List<User> members = partyService.readMembers(partyId);
		return ResponseUtil.JSON(members, HttpStatus.OK);
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
