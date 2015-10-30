package me.singularityfor.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Order on 2015. 10. 30..
 */
@Controller
public class MainController {

    @RequestMapping("/")
    public String home() {
        return "home";
    }

}
