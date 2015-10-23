package me.singularityfor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Order on 2015. 10. 22..
 */
@Controller
public class MainController {

    @RequestMapping("/")
    public String index() {
        return "home";
    }
}
