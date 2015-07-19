package singularity.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	@Resource
	UserController userController;
	
	@RequestMapping("/")
	String home(Model model, HttpSession session) {
		return userController.loginForm(model, session);
	}
	
}