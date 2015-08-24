package singularity.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import singularity.exception.ExistedUserException;
import singularity.exception.FailedLoginException;
import singularity.exception.FailedSendingEmailException;
import singularity.exception.SessionUserMismatchException;
import singularity.user.domain.Confirm;
import singularity.user.domain.User;
import singularity.user.dto.SessionUser;
import singularity.user.service.ConfirmService;
import singularity.user.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/user")
public class UserController {

	@Resource
	UserService userService;
	@Resource
    ConfirmService confirmService;
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	String create(Model model, @Validated User user, BindingResult result) {
		if (result.hasErrors()) {
			return "join";
		}
		try {
            // TODO service로 내리기.
            User dbUser = userService.create(user);
			confirmService.sendMailforSignUp(confirmService.create(dbUser));
		} catch (ExistedUserException | FailedSendingEmailException e) {
			model.addAttribute("existedUserError", e.getMessage());
			return "join";
		}
		model.addAttribute("message", "회원가입에 성공하였습니다. 메일 인증 후 사용 가능합니다.");
		return "sendEmail";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	String login(Model model, User user, HttpSession session) {
		try {
			SessionUser sessionUser = new SessionUser(userService.findForLogin(user));
			session.setAttribute("sessionUser", sessionUser);
			model.addAttribute("user", sessionUser);
		} catch (FailedLoginException e) {
			model.addAttribute("loginFailedError", e.getMessage());
			model.addAttribute("user", user);
			return "login";
		}
		return "party";
	}
	
	@RequestMapping("/logout")
    String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
	@RequestMapping(value="/confirm/{signingKey}", method=RequestMethod.GET)
	String confirm(Model model, @PathVariable String signingKey, HttpSession session) {
        // TODO 서비스 레이어로 내리기
		Confirm confirm = confirmService.findOneByIdentificationKey(signingKey);
        if (null != confirm) {
			userService.accept(confirm.getUser());
		}
		return loginForm(model, session);
	}

	@RequestMapping(value = "", method = RequestMethod.PUT)
	String update(Model model, User user, HttpSession session) {
		try {
			userService.update(user, session);
		} catch (SessionUserMismatchException e) {
			model.addAttribute("errorCode", e.getCause());
			model.addAttribute("errorMessage", e.getMessage());
			return "exception";
		}
		return "user";
	}

	/*
	 * form
	 */
	@RequestMapping(value = "/loginForm", method = RequestMethod.GET)
	public String loginForm(Model model, HttpSession session) {
		if (this.sessionedUser(session)) {
			return "party";
		}
		model.addAttribute("user", new User());
		return "login";
	}

	@RequestMapping(value = "/joinForm", method = RequestMethod.GET)
	String joinForm(Model model, HttpSession session) {
		if (this.sessionedUser(session)) {
			return "party";
		}
		model.addAttribute("user", new User());
		return "join";
	}
	
	private boolean sessionedUser(HttpSession session) {
		if (null != userService.getSessionUser(session)) {
			return true;
		}
		return false;
	}

}
