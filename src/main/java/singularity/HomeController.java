package singularity;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import singularity.user.controller.UserController;

@Controller
public class HomeController {
	@Resource
	UserController userController;
	
	@RequestMapping("/")
	String home() {
		return "home";
	}
	
}