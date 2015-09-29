package singularity;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import singularity.user.controller.UserController;

@Controller
public class HomeController {
	@Resource
	UserController userController;
	
	@RequestMapping("/")
	String home(Model model, HttpSession session) {
		return userController.loginForm(model, session);
	}
	
}