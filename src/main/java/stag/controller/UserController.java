package stag.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import stag.domain.Confirm;
import stag.domain.User;
import stag.exception.ExistedUserException;
import stag.exception.FailedLoginException;
import stag.exception.FailedSendingEmailException;
import stag.exception.SessionUserMismatchException;
import stag.service.MailService;
import stag.service.UserService;

@Controller
@Transactional
@RequestMapping(value = "/user")
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Resource
	UserService userService;
	@Resource
	MailService mailService;

	@RequestMapping(value = "", method = RequestMethod.POST)
	String create(Model model, @Validated User user, BindingResult result) {
		if (result.hasErrors()) {
			return "join";
		}
		
		logger.warn("\nuser={}", user);
		try {
			mailService.sendMailforSignUp(mailService.create(new Confirm(userService.create(user))));
		} catch (ExistedUserException e) {
			model.addAttribute("existedUserError", e.getMessage());
			return "join";
		} catch (FailedSendingEmailException e) {

		}
		model.addAttribute("message", "회원가입에 성공하였습니다. 메일 인증 후 사용 가능합니다.");
		return "sendEmail";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	String login(Model model, User user, HttpSession session) {
		try {
			session.setAttribute("sessionUser", userService.findForLogin(user));
			model.addAttribute("user", userService.findOne(user));
		} catch (FailedLoginException e) {
			model.addAttribute("loginFailedError", e.getMessage());
			model.addAttribute("user", user);
			return "login";
		}
		return "group";
	}
	
	@RequestMapping("/logout")
	protected String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
	@RequestMapping(value="/confirm/{signingKey}", method=RequestMethod.GET)
	String confirm(Model model, @PathVariable String signingKey) {
		Confirm confirm = mailService.findOneBySigningKey(signingKey);
		if (null != confirm) {
			userService.signingUp(confirm.getUser());
		}
		return loginForm(model);
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
	String loginForm(Model model) {
		model.addAttribute("user", new User());
		return "login";
	}

	@RequestMapping(value = "/joinForm", method = RequestMethod.GET)
	String joinForm(Model model) {
		model.addAttribute("user", new User());
		return "join";
	}

}
