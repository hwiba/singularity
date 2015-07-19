package stag.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	@Resource
	UserController userController;
	
	@RequestMapping("/")
	String home(Model model) {
		return userController.loginForm(model);
	}
	
}