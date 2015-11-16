package me.singularityfor.editor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by hyva on 2015. 11. 16..
 */
@Controller
@RequestMapping("/editor")
public class EditorController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getForm() {
        return "editor";
    }

}
