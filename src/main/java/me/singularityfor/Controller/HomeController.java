package me.singularityfor.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Order on 2015. 10. 22..
 */
@Controller
public class HomeController {

    @RequestMapping("/")
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView("home");
        return mav;
    }
}
