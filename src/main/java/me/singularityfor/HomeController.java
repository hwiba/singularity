package me.singularityfor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Order on 2015. 11. 3..
 */

@Slf4j
@Controller
public class HomeController {

    @RequestMapping("/")
    public String home() {
        log.debug("home 접속");
        return "home";
    }
}
