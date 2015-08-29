package singularity.party.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import singularity.common.utility.RequestUtil;
import singularity.common.utility.ResponseUtil;
import singularity.exception.FailedAddingPartyMemberException;
import singularity.exception.PartyLeaveFailedException;
import singularity.exception.UnpermittedAccessException;
import singularity.party.domain.Party;
import singularity.party.service.PartyService;
import singularity.user.domain.User;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

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
		Long userId = RequestUtil.getSessionId(session);
		return ResponseUtil.JSON(partyService.findAllByUserId(userId), HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	protected ResponseEntity<Object> create(@RequestParam final String status, @RequestParam final String partyName,
			HttpSession session, Model model) {
		if (partyName.length() > 15) {
			return ResponseUtil.JSON("그룹명은 15자 이내로 가능합니다", HttpStatus.PRECONDITION_FAILED);
		}
		final Long adminId = RequestUtil.getSessionId(session);
		final Party.Openness openness = "F" == status ? Party.Openness.COMMUNITY : Party.Openness.EVERYONE;
		final Party party = partyService.create(partyName, adminId, openness);
		return ResponseUtil.JSON(party, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{partyId}", method = RequestMethod.DELETE)
	protected ResponseEntity<Object> delete(@PathVariable final Long partyId, HttpSession session, Model model) {
        try {
            partyService.delete(partyId, RequestUtil.getSessionId(session));
        } catch (IllegalArgumentException e) {
            return ResponseUtil.JSON(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
		return ResponseUtil.JSON("", HttpStatus.OK);
	}

	@RequestMapping(value = "/{partyId}/members/invite", method = RequestMethod.POST)
	protected ResponseEntity<Object> inviteGroupMember(@PathVariable Long partyId, @RequestParam Long userId,
			HttpSession session) {
		try {
			partyService.inviteMember(RequestUtil.getSessionId(session), userId, partyId);
		} catch (UnpermittedAccessException | FailedAddingPartyMemberException e) {
			return ResponseUtil.JSON(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return ResponseUtil.JSON("", HttpStatus.OK);
	}

	@RequestMapping(value = "/{partyId}/members/join", method = RequestMethod.POST)
	protected ResponseEntity<Object> joinGroupMember(@PathVariable Long partyId, HttpSession session) {
		partyService.joinMember(RequestUtil.getSessionId(session), partyId);
		return ResponseUtil.JSON("", HttpStatus.OK);
	}

	@RequestMapping(value = "/{partyId}/members/accept", method = RequestMethod.POST)
	protected ResponseEntity<Object> acceptMember(@RequestParam Long userId, @PathVariable Long partyId,
			HttpSession session) {
		partyService.addMember(userId, partyId, RequestUtil.getSessionId(session));
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
	protected ResponseEntity<Object> deleteMember(HttpSession session, @RequestParam Long userId,
			@RequestParam Long partyId) {
		partyService.deleteMember(RequestUtil.getSessionId(session), userId, partyId);
		return ResponseUtil.JSON("", HttpStatus.OK);
	}

	@RequestMapping("/members/{partyId}")
	protected ResponseEntity<Object> getMembers(@PathVariable Long partyId) {
		List<User> members = partyService.getMembers(partyId);
		return ResponseUtil.JSON(members, HttpStatus.OK);
	}

	@RequestMapping("/update/form/{partyId}")
	protected String updateForm(@PathVariable Long partyId, Model model, HttpSession session) {
		Party party = partyService.findOne(partyId);
		Long sessionUserId = RequestUtil.getSessionId(session);
		if (!sessionUserId.equals(party.getAdmin().getId())) {
			return "redirect:/";
		}
		model.addAttribute("party", party);
		model.addAttribute("members", partyService.getMembers(partyId));
		return "updateParty";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	protected String update(@Validated Party party, BindingResult result, @RequestParam MultipartFile backgroundImage, HttpSession session) {
        if (result.hasErrors()) {
            return "/update/form"+String.valueOf(party.getId());
        }
		String path = session.getServletContext().getRealPath("/");
        try {
            partyService.update(RequestUtil.getSessionId(session), party, path, backgroundImage);
        } catch (IOException e) {
            return "/update/form"+String.valueOf(party.getId());
        }
        return "redirect:/g/" + String.valueOf(party.getId());
	}
}
